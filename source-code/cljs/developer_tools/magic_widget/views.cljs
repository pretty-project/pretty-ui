
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.magic-widget.views
    (:require [developer-tools.magic-widget.helpers       :as magic-widget.helpers]
              [developer-tools.event-browser.views        :rename {body event-browser}]
              [developer-tools.re-frame-browser.views     :rename {body re-frame-browser}]
              [developer-tools.request-inspector.views    :rename {body request-inspector}]
              [developer-tools.route-browser.views        :rename {body route-browser}]
              [developer-tools.subscription-browser.views :rename {body subscription-browser}]
              [elements.api                               :as elements]
              [css.api                                    :as css]
              [re-frame.api                               :as r]
              [reagent.api                                :refer [ratom]]
              [vector.api                                 :as vector]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn design-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [deign-mode? @(r/subscribe [:x.db/get-item [:developer-tools :core/meta-items :design-mode?]])]
       [elements/icon-button ::deign-mode-icon-button
                             {:color       (if deign-mode? :primary :muted)
                              :hover-color :highlight
                              :icon        :edit
                              :icon-family :material-symbols-outlined
                              :indent      {:left :xxl}
                              :on-click    [:developer-tools.core/toggle-design-mode!]
                              :label       "Edit"
                              :tooltip     "Toggle the DOM design mode"}]))

(defn toggle-popup-position-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [popup-positions [:tr :br :bl :tl]
        popup-position @(r/subscribe [:x.db/get-item [:developer-tools :magic-widget/popup-position]])]
       [elements/icon-button ::toggle-popup-position-icon-button
                             {:hover-color :highlight
                              :icon        :tab
                              :on-click    []
                              :style       (case popup-position :br {:transform "rotateX(180deg)"}
                                                                :bl {:transform "rotateX(180deg) rotateY(180deg)"}
                                                                :tl {:transform "rotateY(180deg)"}
                                                                :tr {} {})
                              :disabled? true
                              :color :muted
                              :label "Position"}]))

(defn re-frame-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:x.gestures/view-selected? :developer-tools.magic-widget/handler :re-frame-browser])]
       [elements/icon-button ::re-frame-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :database
                              :icon-family :material-symbols-outlined
                              :on-click    [:x.gestures/change-view! :developer-tools.magic-widget/handler :re-frame-browser]
                              :label       "State"}]))

(defn request-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:x.gestures/view-selected? :developer-tools.magic-widget/handler :request-inspector])]
       [elements/icon-button ::request-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :downloading
                              :on-click    [:x.gestures/change-view! :developer-tools.magic-widget/handler :request-inspector]
                              :label       "Requests"}]))

(defn route-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:x.gestures/view-selected? :developer-tools.magic-widget/handler :route-browser])]
       [elements/icon-button ::route-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :route
                              :on-click    [:x.gestures/change-view! :developer-tools.magic-widget/handler :route-browser]
                              :label       "Routes"}]))

(defn re-frame-subscriptions-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:x.gestures/view-selected? :developer-tools.magic-widget/handler :subscription-browser])]
       [elements/icon-button ::re-frame-subscriptions-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :subscriptions
                              :icon-family :material-icons-outlined
                              :on-click    [:x.gestures/change-view! :developer-tools.magic-widget/handler :subscription-browser]
                              :label       "Subs"}]))

(defn re-frame-events-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:x.gestures/view-selected? :developer-tools.magic-widget/handler :event-browser])]
       [elements/icon-button ::re-frame-events-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :data_array
                              :on-click    [:x.gestures/change-view! :developer-tools.magic-widget/handler :event-browser]
                              :label       "Events"}]))

(defn toggle-hide-db-write-count-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [hide-db-write-count? @(r/subscribe [:x.db/get-item [:developer-tools :core/meta-items :hide-db-write-count?]])]
       [elements/icon-button ::toggle-hide-db-write-count-icon-button
                             {:hover-color :highlight
                              :icon        :waterfall_chart
                              :on-click    [:x.db/toggle-item! [:developer-tools :core/meta-items :hide-db-write-count?]]
                              :preset      (if hide-db-write-count? :muted :primary)
                              :label       "Writes"
                              :tooltip     "Display the Re-Frame DB write count"}]))

(defn toggle-print-events-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  ; A re-frame.api/DEBUG-MODE? nem reagent atom, ezért szükséges helyi atomban tárolni a kapcsoló értékét!
  (let [print-events? (ratom @r/DEBUG-MODE?)]
       (fn [] [elements/icon-button ::toggle-print-events-icon-button
                                    {:hover-color :highlight
                                     :icon        :terminal
                                     :on-click    (fn [] (r/toggle-debug-mode!)
                                                         (swap! print-events? not))
                                     :preset      (if @print-events? :primary :muted)
                                     :label       "Print"
                                     :tooltip     "Console print the dispatched Re-Frame events"}])))

(defn close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::close-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                        ;:indent        {:left :xxl}
                         :keypress      {:key-code 27 :required? true}
                         :on-click      [:x.ui/remove-popup! :developer-tools.magic-widget/view]
                         :preset        :close
                         :label         "Close"}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity ::header
                                {:start-content [:<> [re-frame-browser-icon-button]
                                                     [request-browser-icon-button]
                                                     [route-browser-icon-button]
                                                     [re-frame-events-icon-button]
                                                     [re-frame-subscriptions-icon-button]]
                                 :end-content [:<> [design-mode-icon-button]
                                                   [toggle-hide-db-write-count-icon-button]
                                                   [toggle-print-events-icon-button]
                                                   [toggle-popup-position-icon-button]
                                                   [close-icon-button]]
                                 :indent {:bottom :xs}}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(r/subscribe [:x.gestures/get-current-view-id :developer-tools.magic-widget/handler])]
       (case view-id :re-frame-browser     [re-frame-browser]
                     :request-inspector    [request-inspector]
                     :route-browser        [route-browser]
                     :event-browser        [event-browser]
                     :subscription-browser [subscription-browser])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [popup-id]
  [:div {:style {:position "fixed" :top "0" :right "0" :max-height "calc(100vh - 48px)" :max-width "100%"
                 :background "rgba(255, 255, 255, .98)" :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"
                 :display "flex" :flex-direction "column"
                 :border-radius "0 0 0 10px"}}
        [header]
        [:div {:style {:overflow-y "scroll"}}
              [body]]])
