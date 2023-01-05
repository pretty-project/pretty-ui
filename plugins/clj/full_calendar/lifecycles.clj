
(ns full-calendar.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:x.environment/add-css! {:uri "/plugins/full-calendar/common.css"    :js-build :app}]
                                 [:x.environment/add-css! {:uri "/plugins/full-calendar/daygrid.css"   :js-build :app}]
                                 [:x.environment/add-css! {:uri "/plugins/full-calendar/list.css"      :js-build :app}]
                                 [:x.environment/add-css! {:uri "/plugins/full-calendar/timegrid.css"  :js-build :app}]
                                 [:x.environment/add-css! {:uri "/plugins/full-calendar/CUSTOM.css"    :js-build :app}]]}})
