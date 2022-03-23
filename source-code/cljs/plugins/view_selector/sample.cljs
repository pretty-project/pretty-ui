
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
  (r view-selector/change-view! db :my-extension :my-view))

(a/reg-event-db :my-extension.view-selector/change-my-view! change-my-view!)



;; -- A kiválasztott nézet megváltoztatása (B) --------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/change-your-view!
  [:view-selector/change-view! :your-extension :your-view])



;; -- A plugin használata -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-content
  [extension-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id extension-id])]
       (case view-id :my-view   [:div "My view"]
                     :your-view [:div "Your view"]
                                [:div "Default view"])))

(defn my-view
  [surface-id]
  [view-selector/view :my-extension {:allowed-view-ids [:my-view :your-view]
                                     :default-view-id :my-view
                                     :content #'my-content}])
