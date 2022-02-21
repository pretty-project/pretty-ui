
(ns app-extensions.home-screen.views
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-layouts.api :as layouts]

              ; TEMP
              [app-extensions.storage.api :as storage]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people   :on-click [:router/go-to! "/@app-home/clients"]  :badge-color :secondary}
            {:label :products     :icon :category :on-click [:router/go-to! "/@app-home/products"] :badge-color :secondary}
            {:label :file-storage :icon :folder   :on-click [:router/go-to! "/@app-home/storage"]}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
 [:div
  [layouts/layout-b surface-id {:cards CARDS}]
  [storage/media-picker {:label "Borítóképek"
                         :value-path [:xxx]}]])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home-screen/add-item!
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ;  {}
  ;
  ; @usage
  ;  [:home-screen/add-item!]
  (fn [_ [_ item-id item-props]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home-screen/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :home-screen/view {:view #'view}])

(a/reg-event-fx
  :home-screen/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-config-item db :app-title)]
           {:dispatch-n [[:ui/simulate-process!]
                         [:ui/restore-default-window-title!]
                         [:ui/set-header-title! app-title]
                         [:home-screen/render!]]})))
