
(ns app-extensions.trader.log
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-log-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :log]))

(a/reg-sub :trader/get-log-props get-log-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ module message timestamp]]
  (update-in db [:trader :log :log-items] vector/cons-item {:module module :message message :timestamp timestamp}))

(a/reg-event-db :trader/log! log!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- log-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [message module timestamp]}]
  [:pre {:class ".trader--log-item" :style (if (not= module :trader/listener)
                                               {:opacity ".75"}
                                               {:font-weight 600})}
        [:span {:style (styles/log-item-timestamp-style)}
               (time/timestamp-string->time timestamp)]
        [:span {:style (styles/log-item-message-style)}
               (str message)]])

(defn- log-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [log-id {:keys [log-items] :as log-props}]
  (reduce #(conj %1 [log-item log-id log-props %2])
           [:div {:style (styles/log-body-style)}]
           (param log-items)))

(defn- log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [log-id log-props]
  [:<> [:style {:type "text/css"} (styles/log-style-rules)]
       [:div {:style (styles/log-style)}
             [log-items log-id log-props]]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'log
                          :subscriber [:trader/get-log-props]}])
