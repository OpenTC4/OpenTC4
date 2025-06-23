package com.opentc4.research;

import com.opentc4.api.research.IResearchHandler;

public class ResearchManager implements IResearchHandler {

    @Override
    public boolean hasCompleted(String playerUUID, String researchKey) {
        // Placeholder logic, always returns false
        return false;
    }
}
