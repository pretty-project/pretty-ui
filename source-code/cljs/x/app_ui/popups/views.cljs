
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

(defn popup-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [content @(a/subscribe [:ui/get-popup-prop popup-id :content])]
       [:div.x-app-popups--element--content [components/content popup-id content]]))

(defn popup-element-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:div (popups.helpers/popup-attributes popup-id)
        [popup-content                   popup-id]])

(defn popup-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [on-popup-closed   @(a/subscribe [:ui/get-popup-prop popup-id :on-popup-closed])
        on-popup-rendered @(a/subscribe [:ui/get-popup-prop popup-id :on-popup-rendered])]
       (reagent/lifecycles popup-id
                           {:reagent-render         (fn [] [popup-element-structure popup-id])
                            :component-will-unmount (fn [] (a/dispatch on-popup-closed))
                            :component-did-mount    (fn [] (a/dispatch on-popup-rendered))})))



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
