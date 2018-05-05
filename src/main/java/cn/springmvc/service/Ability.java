package cn.springmvc.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import cn.springmvc.dao.UserMapper;
import cn.springmvc.dao.ProjectCommitMapper;
import cn.springmvc.dao.ProjectLanguageMapper;
import cn.springmvc.dao.LanguageMapper;

@Service
public class Ability {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ProjectCommitMapper projectCommitMapper;

	@Autowired
	private ProjectLanguageMapper projectLanguageMapper;

	@Autowired
	private LanguageMapper languageMapper;
	
	final private int threadNum = 2;
	final private int limit = 2;

	public static Map<String,Integer> LangDict = new HashMap<String,Integer>();

	public void calculate() { //Calculate the ability of each user
		List<String> temp = languageMapper.getLangs();
		for(int i = 0; i < temp.size(); i++) LangDict.put(temp.get(i), i);
		int start = Thread.activeCount();
		Monitor monitor = new Monitor(limit, 0, userMapper, projectLanguageMapper);
		Processor[] processors = new Processor[threadNum];
		for(int i = 0; i < threadNum; i++) processors[i] = new Processor(monitor, projectCommitMapper);
		new Thread(monitor).start();
		for(int i = 0; i < threadNum; i++) new Thread(processors[i]).start();
		while(Thread.activeCount() != start);
		System.out.println("Done!");
	}
}

class ProLang {

	final private int langNum = 341;
	final private String[] strConsts = {"language", "bytes"};

	private int project_id;
	private int watchers;
	private int[] languages;
	private double total;

	public ProLang(int id) {
		project_id = id;
		watchers = 1;
		languages = new int[langNum];
		total = 0.0;
	}

	public void setLangs(List<Map<String,Object>> langs) {
		for(Map<String,Object> lang : langs){
			languages[Ability.LangDict.get(lang.get(strConsts[0]))] += (Integer)lang.get(strConsts[1]);
			total += (Integer)lang.get(strConsts[1]);
		}
		if(total < 0.1) total = 1;
	}

	public void getResult(double[] langs) {
		for(int i = 0; i < languages.length && i < langs.length; i++)
			langs[i] = languages[i] / total;
	}

}

class Monitor implements Runnable {

    private UserMapper userMapper;
	private ProjectLanguageMapper projectLanguageMapper;

    private Object synObj;
    private LinkedBlockingQueue<Integer> PidQueue;
    private Map<Integer,ProLang> Pids;

    final private int POOLSIZE = 1000;
	final private int INTERVAL = 50;
	private LinkedBlockingQueue<Integer> IdQueue;

	private Boolean END = false;
	private int offset;
	private int size;
	private List<Integer> userIds;

	public Monitor(int limit, int off, UserMapper ump, ProjectLanguageMapper plm) {
		size = limit;
		offset = off;
		IdQueue = new LinkedBlockingQueue<Integer>();
		userMapper = ump;
		PidQueue = new LinkedBlockingQueue<Integer>();
		Pids = new HashMap<Integer,ProLang>();
		projectLanguageMapper = plm;
	}

	public void run() {
		while(true) {
			if(IdQueue.isEmpty())
				if(!nextSplit())
					break;
			try{
				Thread.sleep(INTERVAL);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		}
		System.out.println("Monitor Terminated.");
		END = true;
	}

	public boolean nextSplit() { //Obtain user's id by offset + limit(size)
		userIds = userMapper.getUserIdOffLim(offset, size);
		if(userIds == null){
			END = true;
			return false;
		}else{
			for(Integer id : userIds)
				IdQueue.offer(id);
			System.out.println(offset);
			offset += size;
			return true;
		}
	}

	public int nextId() {
		Integer result = -2;
		while(true){
			synchronized(IdQueue){
				if(!IdQueue.isEmpty()){
					result = IdQueue.poll();
					break;
				}
			}
			if(END)break;
			try{
				Thread.sleep(INTERVAL / 10);
                        } catch(InterruptedException e){
                                e.printStackTrace();
                        }

		}
		if(END)return -2;
		else return result;
	}

	public ProLang getLang(int pid) {
		ProLang lang = null;
		synchronized(synObj){
			lang = Pids.get(pid);
		}
		if(lang == null) {
			List<Map<String, Object>> langs = projectLanguageMapper.getLangsByProject(pid);
			lang = new ProLang(pid);
			lang.setLangs(langs);
			synchronized(synObj){
				if(PidQueue.size() < POOLSIZE)
					PidQueue.offer(pid);
				else{
					int temp = PidQueue.poll();
					Pids.remove(pid);
				}
				Pids.put(pid, lang);
			}
			return lang;
		}else{
			synchronized(synObj){
				PidQueue.remove(pid);
				PidQueue.offer(pid);
			}
			return lang;
		}
	}

}

class Processor implements Runnable {

    private ProjectCommitMapper projectCommitMapper;

	final private int langNum = 341;
	final private String[] strConsts = { "project_id", "commitNums", "commitTotal" };

	private Monitor monitor;
	private int userId;
	private double[] ability;

	public Processor(Monitor idMonitor, ProjectCommitMapper pcm) {
		monitor = idMonitor;
		projectCommitMapper = pcm;
	}

	public void run() {
		while(true){
			userId = monitor.nextId();
			if(userId == -2)break;
			ability = new double[langNum];
			processUser(); //Calculate the ability of one user
			for(int i = 0; i < ability.length; i++)
				System.out.printf("%f ", ability[i]);
			System.out.println();
			break;
		}
		System.out.println("Thread terminated.");
	}

	private void processUser() {
		System.out.printf("GET %d\n", userId);
		//Obtain projects the user committed, number of commits and total commits of projects
		List<Map<String, Integer>> projects = projectCommitMapper.getProjectCommitsByUser(userId);
		int project_size = projects.size();
		//ids of all projects the user committed
		int[] projectIds = new int[project_size];
		//percentage of user's contribution in each project
		double[] contributes = new double[project_size]; 
		for(int i = 0; i < projects.size(); i++){
			Map<String, Integer> temp = projects.get(i);
			projectIds[i] = temp.get(strConsts[0]);
			contributes[i] = (double)temp.get(strConsts[1])/(double)temp.get(strConsts[2]);
		}
		System.out.printf("Acuqire Project Languages\n");
		//percentage of projects' languages & Obtain it
		double[][] languages = new double[project_size][langNum];
		processProject(projectIds, languages);
		//User's Ability
		for(int i = 0; i < project_size; i++)
			for(int j = 0; j < langNum; j++)
				ability[j] += contributes[i] * languages[i][j];
	}

	private void processProject(int[] projectIds, double[][] languages) {

	}
}
