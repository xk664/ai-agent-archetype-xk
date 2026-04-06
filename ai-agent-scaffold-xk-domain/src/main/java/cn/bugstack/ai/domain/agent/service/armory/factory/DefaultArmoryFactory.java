package cn.bugstack.ai.domain.agent.service.armory.factory;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.node.RootNode;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的装配工厂
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/12/17 08:16
 */
@Service
public class DefaultArmoryFactory {

    @Resource
    private RootNode rootNode;
    @Resource
    private ApplicationContext applicationContext;

    public StrategyHandler<ArmoryCommandEntity, DynamicContext, AiAgentRegisterVO> armoryStrategyHandler() {
        return rootNode;
    }

    public AiAgentRegisterVO getAiAgentRegisterVO(String agentId) {
        return applicationContext.getBean(agentId, AiAgentRegisterVO.class);
    }

    /**
     * 定义一个上下文对象，用于各个节点串联的时候，写入数据和使用数据
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        /**
         * AiApi
         *
         */
        private OpenAiApi aiApi;

        /**
         * ChatModel
         */

        private ChatModel chatModel;



        private Map<String, BaseAgent> AgentGroup=new HashMap<>();

        private Map<String, Object> dataObjects = new HashMap<>();

        private AtomicInteger currentStepIndex =new AtomicInteger(0);
        /**
         * 当前WorkFlow
         */
        private AiAgentConfigTableVO.Module.AgentWorkflow currentAgentWorkflow;

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }

        public List<BaseAgent> queryAgentList(List<String> agentNames){
            List<BaseAgent> baseAgents=new ArrayList<>();
            for(String agentName:agentNames){
                BaseAgent baseAgent=AgentGroup.get(agentName);
                if(baseAgent!=null){
                    baseAgents.add(baseAgent);
                }
            }
            return baseAgents;
        }
        public void addCurrentStepIndex(){
            currentStepIndex.incrementAndGet();
        }

        public int getCurrentStepIndex(){
            return currentStepIndex.get();
        }

    }


}
