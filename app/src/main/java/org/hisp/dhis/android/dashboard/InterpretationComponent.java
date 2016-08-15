/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.dashboard;

import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationCommentEditFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationCommentsFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationCreateFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationEmptyFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationTextEditFragment;
import org.hisp.dhis.android.dashboard.views.fragments.interpretation.InterpretationTextFragment;

import dagger.Subcomponent;

@PerUser
@Subcomponent(
        modules = {
                InterpretationModule.class,

                // Add DashboardModule here for it's interactors
                DashboardModule.class
        }
)
public interface InterpretationComponent {

    //------------------------------------------------------------------------
    // Sub-modules
    //------------------------------------------------------------------------
    // TODO add sub-modules here



    //------------------------------------------------------------------------
    // Injection targets
    //------------------------------------------------------------------------
    // TODO specify injection targets

        //fragments

        void inject(InterpretationEmptyFragment interpretationEmptyFragment);
}

