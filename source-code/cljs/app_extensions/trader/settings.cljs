
(ns app-extensions.trader.settings
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- init-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (return db))

(a/reg-event-db :trader/init-settings! init-settings!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sync-timeout-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/select ::sync-timeout-select
                   {:initial-options engine/SYNC-TIMEOUT-OPTIONS
                    :label "Sync timeout"
                    :get-label-f :label}])

(defn- synchronizing-switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/switch ::synchronizing-switch
                   {:label "Synchronizing"
                    :on-check   [:trader/start-syncing!]
                    :value-path [:trader :sync :active?]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id]
  [:div {:style (styles/overlay-center-style)}
       ;[sync-timeout-select  module-id]
        [synchronizing-switch module-id]
        [elements/horizontal-separator {:size :xxl}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [settings body-id])
