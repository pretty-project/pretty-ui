
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.07
; Description:
; Version: v0.2.0
; Compatibility: x4.4.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]
              [x.app-developer.database-browser :refer [view] :rename {view database-browser}]
              [x.app-developer.request-browser  :refer [view] :rename {view request-browser}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :database-browser)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db (db/path ::primary :selected-view) DEFAULT-VIEW))

(defn- get-header-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:selected-view (r get-selected-view db)})

(a/reg-sub ::get-header-view-props get-header-view-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:selected-view (r get-selected-view db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ view-id]]
  (assoc-in db (db/path ::primary :selected-view) view-id))

(a/reg-event-db ::change-view! change-view!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id {:keys [selected-view] :as view-props}]
  [elements/polarity {:start-content [elements/menu-bar {:menu-items [{:label "DB"
                                                                       :on-click [::change-view! :database-browser]
                                                                       :color (if (not= selected-view :database-browser) :muted)}
                                                                      {:label "Requests"
                                                                       :on-click [::change-view! :request-browser]
                                                                       :color (if (not= selected-view :request-browser)  :muted)}]}]
                      :end-content   [ui/popup-close-icon-button popup-id view-props]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [selected-view]}]
  (case selected-view :database-browser [database-browser]
                      :request-browser  [request-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup! ::view {:content    #'view
                                :label-bar  {:content    #'label-bar
                                             :subscriber [::get-header-view-props]}
                                :layout     :boxed
                                :subscriber [::get-view-props]}])
