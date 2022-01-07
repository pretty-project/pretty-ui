
(ns x.app-developer.developer-tools
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-gestures.api :as gestures]
              [x.app-ui.api       :as ui]
              [x.app-developer.database-browser :rename {body database-browser}]
              [x.app-developer.request-browser  :rename {body request-browser}]
              [x.app-developer.route-browser    :rename {body route-browser}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW-ID :database-browser)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label    "DB"
    :on-click [:gestures/change-view! ::handler :database-browser]
    :active?  (= view-id :database-browser)}
   {:label    "Requests"
    :on-click [:gestures/change-view! ::handler :request-browser]
    :active?  (= view-id :request-browser)}
   {:label    "Routes"
    :on-click [:gestures/change-view! ::handler :route-browser]
    :active?  (= view-id :route-browser)}])
;   {:label "Docs"
;    :on-click [:router/go-to! "/@app-home/docs"]



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [debug-mode (r a/get-debug-mode db)]
       {:print-events? (= debug-mode "pineapple-juice")
        :view-id       (r gestures/get-selected-view-id db ::handler)}))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:view-id (r gestures/get-selected-view-id db ::handler)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-print-events-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [print-events?]}]
  [elements/button ::toggle-event-print-button
                   {:preset (if print-events? :primary-icon-button :muted-icon-button)
                    :icon :terminal :tooltip "Print events"
                    :on-click [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/polarity {:start-content [elements/menu-bar {:menu-items (menu-items header-id header-props)}]
                      :end-content   [:<> [toggle-print-events-button header-id header-props]
                                          [ui/popup-close-icon-button header-id header-props]]}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  (case view-id :database-browser [database-browser]
                :request-browser  [request-browser]
                :route-browser    [route-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db ::handler {:default-view-id DEFAULT-VIEW-ID})
       :dispatch [:ui/add-popup! ::view
                                 {:body   {:content #'body   :subscriber [::get-body-props]}
                                  :header {:content #'header :subscriber [::get-header-props]}}]}))
