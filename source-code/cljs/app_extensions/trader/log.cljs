
(ns app-extensions.trader.log
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.time      :as time]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-log-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :log])
         (r sync/get-response db :trader/get-log-data)))

(a/reg-sub :trader/get-log-props get-log-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- log-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [module-id log-entry timestamp warning?]}]
  [:div {:class ".trader--log-item" :style {:display "flex"}}
        [:pre {:style (styles/log-item-timestamp-style warning?)}
              (time/timestamp-string->time timestamp)]
        [:pre {:style (styles/log-item-message-style warning?)}
              (str log-entry)]])

(defn- log-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [log-items] :as module-props}]
  (reduce #(conj %1 [log-item module-id module-props %2])
           [:div {:style (styles/log-body-style)}]
           (param log-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:<> [:div {:style (styles/log-style)}
             [log-items module-id module-props]]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/log
                         {:component  #'log
                          :subscriber [:trader/get-log-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/connect-to-log!
  [:trader/add-subscription! :trader/log
                             {:query      [`(:trader/get-log-data ~{})]
                              :target-paths {:trader/get-log-data [:trader :log]}}])
