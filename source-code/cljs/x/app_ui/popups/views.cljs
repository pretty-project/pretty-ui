
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
  ; @param (map) button-props
  ;  {:on-accept (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [ui/popup-accept-button :my-popup {...}]
  [popup-id {:keys [on-accept]}]
  [elements/button {:indent   {:horizontal :xxs :vertical :xs}
                    :keypress {:key-code 13}
                    :on-click (if on-accept {:dispatch-n [on-accept [:ui/close-popup! popup-id]]}
                                            [:ui/close-popup! popup-id])
                    :preset   :accept}])

(defn popup-save-button
  ; @param (keyword) popup-id
  ; @param (map) button-props
  ;  {:on-save (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [ui/popup-save-button :my-popup {...}]
  [popup-id {:keys [on-save]}]
  [elements/button {:indent   {:horizontal :xxs :vertical :xs}
                    :keypress {:key-code 13}
                    :on-click (if on-save {:dispatch-n [on-save [:ui/close-popup! popup-id]]}
                                          [:ui/close-popup! popup-id])
                    :preset   :save}])

(defn popup-cancel-button
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [ui/popup-cancel-button :my-popup]
  [popup-id]
  [elements/button {:indent   {:horizontal :xxs :vertical :xs}
                    :keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel}])

(defn popup-go-up-icon-button
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [ui/popup-go-up-icon-button :my-popup]
  [_]
  [elements/icon-button {:on-click [:router/go-up!]
                         :preset   :back}])

(defn popup-go-back-icon-button
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [ui/popup-go-back-icon-button :my-popup]
  [_]
  [elements/icon-button {:on-click [:router/go-back!]
                         :preset   :back}])

(defn popup-close-icon-button
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [ui/popup-close-icon-button :my-popup]
  [popup-id]
  [elements/icon-button {:keypress {:key-code 27}
                         :on-click [:ui/close-popup! popup-id]
                         :preset   :close}])

(defn popup-placeholder-icon-button
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [ui/popup-placeholder-icon-button :my-popup]
  [_]
  [elements/icon-button {:variant :placeholder}])

(defn popup-label
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/popup-label :my-popup {...}]
  [_ {:keys [label]}]
  (if label [elements/label {:content     label
                             :font-weight :extra-bold
                             :indent      {:horizontal :xs}}]))

(defn accept-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:on-accept (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [ui/accept-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:end-content [popup-accept-button popup-id bar-props]}])

(defn save-popup-header
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:label (metamorphic-content)(opt)
  ;   :on-save (metamorphic-event)}
  ;
  ; @usage
  ;  [ui/save-popup-header :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content  [popup-cancel-button popup-id]
                                 :middle-content [popup-label         popup-id bar-props]
                                 :end-content    [popup-save-button   popup-id bar-props]}])

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
  [popup-id]
  (if-let [minimizable? @(a/subscribe [:ui/get-popup-prop popup-id :minimizable?])]
          [elements/icon-button {:class    :x-app-popups--element--minimize-button
                                 :color    :invert
                                 :icon     :close_fullscreen
                                 :on-click [:ui/minimize-popup! popup-id]}]))

(defn- popup-maximize-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (if-let [minimized? @(a/subscribe [:ui/get-popup-prop popup-id :minimized?])]
          [elements/icon-button {:class    :x-app-popups--element--maximize-button
                                 :color    :muted
                                 :icon     :fullscreen
                                 :on-click [:ui/maximize-popup! popup-id]}]))

(defn- popup-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:<> (if-let [render-touch-anchor? @(a/subscribe [:ui/render-touch-anchor? popup-id])]
               [:div.x-app-popups--element--touch-anchor])
       (if-let [header @(a/subscribe [:ui/get-popup-prop popup-id :header])]
               [:div.x-app-popups--element--header [components/content popup-id header]]
               [:div.x-app-popups--element--header-placeholder])])



;; -- Popup body components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [body @(a/subscribe [:ui/get-popup-prop popup-id :body])]
       [:div.x-app-popups--element--body (popups.helpers/popup-body-attributes popup-id)
                                         [components/content                   popup-id body]]))



;; -- Popup footer components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (if-let [footer @(a/subscribe [:ui/get-popup-prop popup-id :footer])]
          [:div.x-app-popups--element--footer [components/content popup-id footer]]
          [:div.x-app-popups--element--footer-placeholder]))



;; -- Popup layout components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (if-let [render-popup-cover? @(a/subscribe [:ui/render-popup-cover? popup-id])]
          (if-let [user-close? @(a/subscribe [:ui/get-popup-prop popup-id :user-close?])]
                  [:div.x-app-popups--element--cover {:on-click #(a/dispatch [:ui/close-popup! popup-id])}]
                  [:div.x-app-popups--element--cover])))

(defn- unboxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:div.x-app-popups--element--structure [popup-body   popup-id]
                                         [popup-header popup-id]
                                         [popup-footer popup-id]])

(defn- boxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  [:div.x-app-popups--element--structure [:div.x-app-popups--element--background]
                                         [popup-header          popup-id]
                                         [popup-body            popup-id]
                                         [popup-footer          popup-id]
                                         [popup-minimize-button popup-id]])

(defn- popup-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (let [layout @(a/subscribe [:ui/get-popup-prop popup-id :layout])]
       (case layout :boxed   [boxed-popup-structure   popup-id]
                    :flip    [boxed-popup-structure   popup-id]
                    :unboxed [unboxed-popup-structure popup-id])))

(defn- popup-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  [popup-id popup-props]
  [:<> [popup-maximize-button popup-id]
       [:div (popups.helpers/popup-attributes popup-id popup-props)
             [popup-cover                     popup-id]
             [popup-layout                    popup-id]]])



;; -- Renderer components -----------------------------------------------------
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
