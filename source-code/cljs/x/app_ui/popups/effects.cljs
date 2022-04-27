
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.effects
    (:require [mid-fruits.candy           :refer [param]]
              [x.app-core.api             :as a :refer [r]]
              [x.app-ui.popups.prototypes :as popups.prototypes]
              [x.app-ui.renderer          :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/render-popup!
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ;  {:body (metamorphic-content)
  ;   :footer (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :layout (keyword)(opt)
  ;    :boxed, :unboxed, :flip
  ;    Default: :boxed
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-popup-rendered (metamorphic-event)(opt)
  ;   :render-exclusive? (boolean)(opt)
  ;    Default: false
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :none
  ;    Only w/ {:layout :boxed}
  ;   :user-close? (boolean)(opt)
  ;    A popup felület bezárható a surface cover felületre kattintva
  ;    Default: true}
  ;
  ; @usage
  ;  [:ui/render-popup! {...}]
  ;
  ; @usage
  ;  [:ui/render-popup! :my-popup {...}]
  ;
  ; @usage
  ;  (defn my-body   [popup-id]              [:div "My body"])
  ;  (defn my-header [popup-id header-props] [:div "My header"])
  ;  [:ui/render-popup! {:body   {:content #'my-body}
  ;                      :header {:content #'my-header :subscriber [:get-my-header-props]}}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ popup-id popup-props]]
      (let [popup-props (r popups.prototypes/popup-props-prototype db popup-id popup-props)]
           (if-let [render-exclusive? (get popup-props :render-exclusive?)]
                   [:ui/render-popup-exclusive! popup-id popup-props]
                   [:ui/render-popup-element!   popup-id popup-props]))))

(a/reg-event-fx
  :ui/close-popup!
  ; @param (keyword) popup-id
  ;
  ; @usage
  ;  [:ui/close-popup! :my-popup]
  (fn [{:keys [db]} [_ popup-id]]
      {:dispatch-n [[:ui/destroy-element! :popups popup-id]
                    ; Eltávolítja a popup-id azonosítójú popup felület által elhelyezett scroll-tiltást
                    [:environment/remove-scroll-prohibition! popup-id]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/render-popup-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  (fn [_ [_ popup-id popup-props]]
      {:dispatch-n [[:ui/request-rendering-element! :popups popup-id popup-props]
                    ; A popup-id azonosítójú popup felület által elhelyez egy scroll-tiltást
                    [:environment/add-scroll-prohibition! popup-id]]}))

(a/reg-event-fx
  :ui/render-popup-exclusive!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  (fn [{:keys [db]} [_ popup-id popup-props]]
      (let [close-popups-duration  (r renderer/get-visible-elements-destroying-duration db :popups)
            close-surface-duration (r renderer/get-visible-elements-destroying-duration db :surface)
            close-surface-delay    (param close-popups-duration)
            render-popup-delay     (+ close-popups-duration close-surface-duration)]
           {:dispatch-later
            [{:ms                   0 :dispatch [:ui/destroy-all-elements! :popups]}
             {:ms close-surface-delay :dispatch [:ui/destroy-all-elements! :surface]}
             {:ms  render-popup-delay :dispatch [:ui/render-popup-element! popup-id popup-props]}]})))
