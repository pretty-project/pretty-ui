
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.views
    (:require [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-elements.api      :as elements]
              [x.app-environment.api   :as environment]
              [x.app-ui.popups.helpers :as popups.helpers]
              [x.app-ui.renderer       :rename {component renderer}]))



;; -- Preset components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-accept-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-accept-icon-button :my-popup {...}]
  [popup-id _]
  [elements/button {:keypress {:key-code 13}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :accept-button
                    :indent   :right}])

(defn popup-cancel-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-cancel-icon-button :my-popup {...}]
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel-button
                    :indent   :left}])

(defn popup-go-up-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-go-up-icon-button :my-popup {...}]
  [_ _]
  [elements/button {:on-click [:router/go-up!]
                    :preset   :back-icon-button}])

(defn popup-go-back-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-go-back-icon-button :my-popup {...}]
  [_ _]
  [elements/button {:on-click [:router/go-back!]
                    :preset   :back-icon-button}])

(defn popup-close-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-close-icon-button :my-popup {...}]
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :close-icon-button}])

(defn popup-placeholder-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-placeholder-icon-button :my-popup {...}]
  [_ _]
  [elements/icon-button {:layout :icon-button :variant :placeholder}])

(defn popup-label
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/popup-label :my-popup {...}]
  [_ {:keys [label]}]
  (if label [elements/label {:content label}]))

(defn accept-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/accept-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:end-content [popup-accept-button popup-id bar-props]}])

(defn cancel-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/cancel-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [popup-cancel-button popup-id bar-props]}])

(defn close-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/close-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content  [popup-placeholder-icon-button popup-id bar-props]
                                 :middle-content [popup-label                   popup-id bar-props]
                                 :end-content    [popup-close-icon-button       popup-id bar-props]}])

(defn go-up-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/go-up-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-go-up-icon-button popup-id bar-props]
                                                     [popup-label             popup-id bar-props]]}])

(defn go-back-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/go-back-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-go-back-icon-button popup-id bar-props]
                                                     [popup-label               popup-id bar-props]]}])



;; -- Popup header components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-minimize-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimizable? (boolean)(opt)}
  [popup-id {:keys [minimizable?]}]
  (if minimizable? [elements/button {:class    :x-app-popups--element--minimize-button
                                     :color    :invert
                                     :icon     :close_fullscreen
                                     :layout   :icon-button
                                     :on-click [:ui/minimize-popup! popup-id]
                                     :variant  :transparent}]))

(defn- popup-maximize-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)}
  [popup-id {:keys [minimized?]}]
  (if minimized? [elements/button {:class    :x-app-popups--element--maximize-button
                                   :color    :muted
                                   :icon     :fullscreen
                                   :layout   :icon-button
                                   :on-click [:ui/maximize-popup! popup-id]
                                   :variant  :filled}]))

(defn- popup-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:header (map)(opt)
  ;   :render-touch-anchor? (boolean)}
  [popup-id {:keys [header render-touch-anchor?] :as popup-props}]
  [:<> (if render-touch-anchor? [:div.x-app-popups--element--touch-anchor])
       (if header               [:div.x-app-popups--element--header [components/content popup-id header]]
                                [:div.x-app-popups--element--header-placeholder])])

(defn popup-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  [components/subscriber popup-id
                         {:render-f   #'popup-header-structure
                          :base-props popup-props
                          :subscriber [:ui/get-popup-header-props popup-id]}])



;; -- Popup body components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:body (map)}
  [popup-id {:keys [body] :as popup-props}]
  [:div.x-app-popups--element--body (popups.helpers/popup-body-attributes popup-id popup-props)
                                    [components/content                  popup-id body]])



;; -- Popup layout components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:user-close? (boolean)}
  [popup-id {:keys [user-close?]}]
  (if user-close? [:div.x-app-popups--element--cover {:on-click #(a/dispatch [:ui/close-popup! popup-id])}]
                  [:div.x-app-popups--element--cover]))

(defn- unboxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  [:div.x-app-popups--element--structure
    [popup-body   popup-id popup-props]
    [popup-header popup-id popup-props]])

(defn- boxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  [:div.x-app-popups--element--structure
    [:div.x-app-popups--element--background]
    [popup-header          popup-id popup-props]
    [popup-body            popup-id popup-props]
    [popup-minimize-button popup-id popup-props]])

(defn- popup-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:layout (keyword)}
  [popup-id {:keys [layout] :as popup-props}]
  (case layout :boxed   [boxed-popup-structure   popup-id popup-props]
               :flip    [boxed-popup-structure   popup-id popup-props]
               :unboxed [unboxed-popup-structure popup-id popup-props]))

(defn- popup-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)}
  [popup-id {:keys [minimized?] :as popup-props}]
  [:<> [popup-maximize-button popup-id popup-props]
       [:div (popups.helpers/popup-attributes popup-id popup-props)
             [popup-cover                     popup-id popup-props]
             [popup-layout                    popup-id popup-props]]])



;; -- Renderer components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :popups {:alternate-renderer-id :surface
                     :element               #'popup-element
                     :max-elements-rendered 5
                     :required?             true
                     :queue-behavior        :push
                     :rerender-same?        false}])
