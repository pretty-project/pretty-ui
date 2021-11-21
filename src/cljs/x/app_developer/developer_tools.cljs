
(ns x.app-developer.developer-tools
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-gestures.api :as gestures]
              [x.app-ui.api       :as ui]
              [x.app-developer.database-browser :rename {view database-browser}]
              [x.app-developer.request-browser  :rename {view request-browser}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :database-browser)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:selected-view (r gestures/get-selected-view db ::handler)})

(a/reg-sub ::get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:selected-view (r gestures/get-selected-view db ::handler)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [selected-view] :as header-props}]
  [elements/polarity {:start-content [elements/menu-bar {:menu-items [{:label "DB"
                                                                       :on-click [:gestures/change-view! ::handler :database-browser]
                                                                       :color (if (not= selected-view :database-browser) :muted)}
                                                                      {:label "Requests"
                                                                       :on-click [:gestures/change-view! ::handler :request-browser]
                                                                       :color (if (not= selected-view :request-browser)  :muted)}]}]
                      :end-content   [ui/popup-close-icon-button header-id header-props]}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [selected-view]}]
  (case selected-view :database-browser [database-browser]
                      :request-browser  [request-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db ::handler {:default-view DEFAULT-VIEW})
       :dispatch [:x.app-ui/add-popup! ::view
                                       {:content    #'body
                                        :label-bar  {:content    #'header
                                                     :subscriber [::get-header-props]}
                                        :layout     :boxed
                                        :subscriber [::get-body-props]}]}))
