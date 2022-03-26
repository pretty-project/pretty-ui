
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.sample
    (:require [plugins.view-selector.api :as view-selector]
              [x.app-core.api            :as a]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin használatához mindenképpen szükséges a szerver-oldali [:view-selector/init-selector! ...]
; eseményt alkalmazni!



;; -- A kiválasztott nézet megváltoztatása (A) --------------------------------
;; ----------------------------------------------------------------------------

(defn change-my-view!
  [db _]
  (r view-selector/change-view! db :my-selector :my-view))

(a/reg-event-db :my-selector/change-my-view! change-my-view!)



;; -- A kiválasztott nézet megváltoztatása (B) --------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :your-selector/change-your-view!
  [:view-selector/change-view! :your-selector :your-view])



;; -- A plugin használata -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-content
  [selector-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id selector-id])]
       (case view-id :my-view   [:div "My view"]
                     :your-view [:div "Your view"]
                                [:div "Default view"])))

(defn my-view
  [surface-id]
  [view-selector/view :my-selector {:allowed-view-ids [:my-view :your-view]
                                    :default-view-id :my-view
                                    :content #'my-content}])
