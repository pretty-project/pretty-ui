
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.views
    (:require [re-frame.api            :as r]
              [reagent.api             :as reagent]
              [x.app-components.api    :as x.components]
              [x.app-ui.popups.helpers :as popups.helpers]
              [x.app-ui.renderer       :rename {component renderer}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-debug-tools
  [popup-id]
  (if-let [debug-mode-detected? @(r/subscribe [:core/debug-mode-detected?])]
          (let [popup-minimized?    @(r/subscribe [:ui/get-popup-prop popup-id :minimized?])
                popup-stick-to-top? @(r/subscribe [:ui/get-popup-prop popup-id :stick-to-top?])]
               [:div {:class :x-app-popups--element--debug-tools}
                     (if popup-minimized? [:div {:class :x-app-popups--element--debug-button :on-click #(r/dispatch [:ui/maximize-popup! popup-id])}
                                                "Maximize"]
                                          [:div {:class :x-app-popups--element--debug-button :on-click #(r/dispatch [:ui/minimize-popup! popup-id])}
                                                "Minimize"])
                     (if popup-stick-to-top? [:div {:class :x-app-popups--element--debug-button :on-click #(r/dispatch [:ui/stick-popup-to-top! popup-id])}
                                                   "Stick to top"]
                                             [:div {:class :x-app-popups--element--debug-button :on-click #(r/dispatch [:ui/stick-popup-to-top! popup-id])}
                                                   "Stick to top"])])))

(defn popup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [content @(r/subscribe [:ui/get-popup-prop popup-id :content])]
       [:div.x-app-popups--element--content [x.components/content popup-id content]]))

(defn popup-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:<> [:div (popups.helpers/popup-attributes popup-id)
             [popup-content                   popup-id]]
       [popup-debug-tools popup-id]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :popups {:alternate-renderer    :surface
                     :element               #'popup-element
                     :max-elements-rendered 5
                     :required?             true
                     :queue-behavior        :push
                     :rerender-same?        false}])
