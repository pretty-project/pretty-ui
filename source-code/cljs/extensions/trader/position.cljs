
(ns extensions.trader.position
    (:require [extensions.trader.styles :as styles]
              [extensions.trader.sync   :as sync]
              [mid-fruits.candy         :refer [param return]]
              [mid-fruits.map           :refer [dissoc-in]]
              [mid-fruits.string        :as string]
              [x.app-components.api     :as components]
              [x.app-core.api           :as a :refer [r]]
              [x.app-elements.api       :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-position-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :position])
         (r sync/get-response db :trader/get-position-data)
         {:responsed? (r sync/responsed? db :trader/get-position-data)}))

(a/reg-sub :trader/get-position-props get-position-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- position
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props])


(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/position
                         {:render-f   #'position
                          :subscriber [:trader/get-position-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/connect-to-position!
  (fn [_ _]
      [:trader/subscribe-to-query! :trader/position
                                   {:query [`(:trader/download-position-data ~{})]}]))
