
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.views
    (:require [mid-fruits.css                          :as css]
              [mid-fruits.vector                       :as vector]
              [re-frame.api                            :as r]
              [x.app-developer.developer-tools.helpers :as developer-tools.helpers]
              [x.app-developer.event-browser.views     :rename {body event-browser}]
              [x.app-developer.re-frame-browser.views  :rename {body re-frame-browser}]
              [x.app-developer.request-inspector.views :rename {body request-inspector}]
              [x.app-developer.route-browser.views     :rename {body route-browser}]
              [x.app-elements.api                      :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-popup-position-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [popup-positions [:tr :br :bl :tl]
        popup-position @(r/subscribe [:db/get-item [:developer :developer-tools/popup-position]])]
       [elements/icon-button ::toggle-popup-position-icon-button
                             {:hover-color :highlight
                              :icon        :tab
                              :on-click    []
                              :style       (case popup-position :br {:transform "rotateX(180deg)"}
                                                                :bl {:transform "rotateX(180deg) rotateY(180deg)"}
                                                                :tl {:transform "rotateY(180deg)"}
                                                                :tr {} {})

                              :disabled? true
                              :color :muted}]))

(defn toggle-print-events-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [print-events? @(r/subscribe [:developer-tools/print-events?])]
       [elements/icon-button ::toggle-print-events-icon-button
                             {:hover-color :highlight
                              :icon        :terminal
                              :indent      {:left :xxl}
                              :on-click    [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]
                              :preset      (if print-events? :primary :muted)}]))

(defn close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::close-icon-button
                        {:border-radius :s
                         :hover-color   :highlight
                         :keypress      {:key-code 27 :required? true}
                         :on-click      [:ui/close-popup! :developer.developer-tools/view]
                         :preset        :close}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity ::header
                                {:start-content [elements/menu-bar {:menu-items (developer-tools.helpers/menu-items)}]
                                 :end-content   [:<> [toggle-print-events-icon-button]
                                                     [toggle-popup-position-icon-button]
                                                     [close-icon-button]]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(r/subscribe [:gestures/get-current-view-id :developer.developer-tools/handler])]
       (case view-id :re-frame-browser  [re-frame-browser]
                     :request-inspector [request-inspector]
                     :route-browser     [route-browser]
                     :event-browser     [event-browser])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [popup-id]
  [:div {:style {:position "fixed" :top "0" :right "0" :max-height "100vh" :max-width "100%"
                 :background "rgba(255, 255, 255, .98)" :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"
                 :display "flex" :flex-direction "column"
                 :border-radius "0 0 0 10px"}}
        [header]
        [:div {:style {:overflow-y "scroll"}}
              [body]]])
