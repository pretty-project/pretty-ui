
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.magic-widget.views
    (:require [developer-tools.magic-widget.helpers    :as magic-widget.helpers]
              [developer-tools.event-browser.views     :rename {body event-browser}]
              [developer-tools.re-frame-browser.views  :rename {body re-frame-browser}]
              [developer-tools.request-inspector.views :rename {body request-inspector}]
              [developer-tools.route-browser.views     :rename {body route-browser}]
              [elements.api                            :as elements]
              [mid-fruits.css                          :as css]
              [mid-fruits.vector                       :as vector]
              [re-frame.api                            :as r]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn design-mode-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [deign-mode? @(r/subscribe [:db/get-item [:developer-tools :core/meta-items :design-mode?]])]
       [elements/icon-button ::deign-mode-icon-button
                             {:color       (if deign-mode? :primary :muted)
                              :hover-color :highlight
                              :icon        :edit
                              :icon-family :material-symbols-outlined
                              :indent      {:left :xxl}
                              :on-click    [:developer-tools.core/toggle-design-mode!]
                              :label       "Edit"}]))

(defn toggle-popup-position-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [popup-positions [:tr :br :bl :tl]
        popup-position @(r/subscribe [:db/get-item [:developer-tools :magic-widget/popup-position]])]
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
  (let [view-selected? @(r/subscribe [:gestures/view-selected? :developer-tools.magic-widget/handler :re-frame-browser])]
       [elements/icon-button ::re-frame-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :database
                              :icon-family :material-symbols-outlined
                              :on-click    [:gestures/change-view! :developer-tools.magic-widget/handler :re-frame-browser]
                              :label       "State"}]))

(defn request-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:gestures/view-selected? :developer-tools.magic-widget/handler :request-inspector])]
       [elements/icon-button ::request-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :downloading
                              :on-click    [:gestures/change-view! :developer-tools.magic-widget/handler :request-inspector]
                              :label       "Requests"}]))

(defn route-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:gestures/view-selected? :developer-tools.magic-widget/handler :route-browser])]
       [elements/icon-button ::route-browser-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :route
                              :on-click    [:gestures/change-view! :developer-tools.magic-widget/handler :route-browser]
                              :label       "Routes"}]))

(defn re-frame-events-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-selected? @(r/subscribe [:gestures/view-selected? :developer-tools.magic-widget/handler :event-browser])]
       [elements/icon-button ::re-frame-events-icon-button
                             {:color       (if view-selected? :default :muted)
                              :hover-color :highlight
                              :icon        :data_array
                              :on-click    [:gestures/change-view! :developer-tools.magic-widget/handler :event-browser]
                              :label       "Events"}]))

(defn toggle-print-events-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [print-events? @(r/subscribe [:developer-tools.core/print-events?])]
       [elements/icon-button ::toggle-print-events-icon-button
                             {:hover-color :highlight
                              :icon        :terminal
                              :on-click    [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]
                              :preset      (if print-events? :primary :muted)
                              :label       "Print"}]))

(defn close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::close-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                        ;:indent        {:left :xxl}
                         :keypress      {:key-code 27 :required? true}
                         :on-click      [:ui/remove-popup! :developer-tools.magic-widget/view]
                         :preset        :close
                         :label         "Close"}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity ::header
                                {:start-content [:<> [re-frame-browser-icon-button]
                                                     [request-browser-icon-button]
                                                     [route-browser-icon-button]
                                                     [re-frame-events-icon-button]]
                                 :end-content [:<> [design-mode-icon-button]
                                                   [toggle-print-events-icon-button]
                                                   [toggle-popup-position-icon-button]
                                                   [close-icon-button]]
                                 :indent {:bottom :xs}}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(r/subscribe [:gestures/get-current-view-id :developer-tools.magic-widget/handler])]
       (case view-id :re-frame-browser  [re-frame-browser]
                     :request-inspector [request-inspector]
                     :route-browser     [route-browser]
                     :event-browser     [event-browser])))



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
