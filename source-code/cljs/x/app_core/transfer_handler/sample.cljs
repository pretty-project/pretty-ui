
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.transfer-handler.sample
    (:require [x.app-core.api :as a [:refer] r]))



;; -- Fogadott adatok olvasása ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-my-transfer-data
  [db _]
  (r a/get-transfer-data db :my-transfer))

(defn get-my-transfer-item
  [db _]
  (r a/get-transfer-item db :my-transfer :my-item))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component
  []
  (let [my-transfer-data @(a/subscribe [:core/get-transfer-data :my-transfer])
        my-transfer-item @(a/subscribe [:core/get-transfer-item :my-transfer :my-item])]
       [:div (str "My data: " my-transfer-data)
             (str "My item: " my-transfer-item)]))
