
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.views
    (:require [reagent.api             :as reagent]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a]
              [x.app-ui.popups.helpers :as popups.helpers]
              [x.app-ui.renderer       :rename {component renderer}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-size-button
  [popup-id]
  (if-let [debug-mode-detected? @(a/subscribe [:core/debug-mode-detected?])]
          (if-let [popup-minimized? @(a/subscribe [:ui/get-popup-prop popup-id :minimized?])]
                  [:div.x-app-popups--element--maximize-button {:on-click #(a/dispatch [:ui/maximize-popup! popup-id])}
                                                               "Maximize"]
                  [:div.x-app-popups--element--minimize-button {:on-click #(a/dispatch [:ui/minimize-popup! popup-id])}
                                                               "Minimize"])))

(defn popup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [content @(a/subscribe [:ui/get-popup-prop popup-id :content])]
       [:div.x-app-popups--element--content [components/content popup-id content]]))

(defn popup-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:<> [:div (popups.helpers/popup-attributes popup-id)
             [popup-content                   popup-id]]
       [popup-size-button popup-id]])



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
