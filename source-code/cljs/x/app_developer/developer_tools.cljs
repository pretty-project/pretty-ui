
(ns x.app-developer.developer-tools
    (:require [x.app-components.api :as components]
              [x.app-core.api      :as a :refer [r]]
              [x.app-db.api        :as db]
              [x.app-elements.api  :as elements]
              [x.app-gestures.api  :as gestures]
              [x.app-ui.api        :as ui]
              [x.app-developer.database-browser        :rename {body database-browser}]
              [x.app-developer.request-inspector.views :rename {body request-inspector}]
              [x.app-developer.route-browser           :rename {body route-browser}]))



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
    :on-click [:gestures/change-view! ::handler :request-inspector]
    :active?  (= view-id :request-inspector)}
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

(a/reg-sub :developer-tools/get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:view-id (r gestures/get-selected-view-id db ::handler)})

(a/reg-sub :developer-tools/get-body-props get-body-props)



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-print-events-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [print-events?]}]
  [elements/button ::toggle-event-print-button
                   {:preset (if print-events? :primary-icon-button :muted-icon-button)
                    :icon :terminal :tooltip "Print events"
                    :on-click [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]}])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/horizontal-polarity {:start-content [elements/menu-bar {:menu-items (menu-items header-id header-props)}]
                                 :end-content   [:<> [toggle-print-events-button header-id header-props]
                                                     [ui/popup-close-icon-button ::view]]}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [components/subscriber ::header
                         {:render-f   #'header-structure
                          :subscriber [:developer-tools/get-header-props]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  (case view-id :database-browser  [database-browser]
                :request-inspector [request-inspector]
                :route-browser     [route-browser]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [components/subscriber ::body
                         {:render-f   #'body-structure
                          :subscriber [:developer-tools/get-body-props]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db ::handler {:default-view-id DEFAULT-VIEW-ID})
       :dispatch [:ui/add-popup! ::view
                                 {:body   #'body
                                  :header #'header
                                  :stretch-orientation :vertical}]}))
