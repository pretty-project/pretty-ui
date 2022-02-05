
(ns app-extensions.home.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-layouts.api    :as layouts]

              ; TEMP
              [app-extensions.storage.api :as storage]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people   :on-click [:router/go-to! "/@app-home/clients"]  :badge-color :secondary}
            {:label :products     :icon :category :on-click [:router/go-to! "/@app-home/products"] :badge-color :secondary}
            {:label :file-storage :icon :folder   :on-click [:router/go-to! "/@app-home/storage"]}])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _])

(a/reg-sub :home/get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [user-name] :as view-props}]
 [:div
  [layouts/layout-b surface-id {:cards CARDS}]
  [storage/media-picker {:label "Borítóképek"
                         :value-path [:xxx]}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [components/subscriber surface-id
                         {:render-f   #'view-structure
                          :subscriber [:home/get-view-props]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :home/view {:view #'view}])

(a/reg-event-fx
  :home/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-config-item db :app-title)]
           {:dispatch-n [[:ui/restore-default-window-title!]
                         [:ui/set-header-title! app-title]
                         [:home/render!]]})))
