
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.effects
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-core.api       :as a :refer [r]]
              [x.app-ui.popups.subs :as popups.subs]
              [x.app-ui.renderer    :as renderer]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ;  {:autopadding? (boolean)
  ;   :hide-animated? (boolean)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :min-width (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [db [_ popup-id popup-props]]
  (merge {:autopadding?     true
          :hide-animated?   true
          :horizontal-align :center
          :layout           :boxed
          :min-width        :m
          :reveal-animated? true
          :update-animated? false
          :user-close?      true}
         (param popup-props)
         (if (r popups.subs/flip-layout-anyway? db popup-id popup-props)
             {:layout :flip})
         (if ; DEBUG
             (r a/debug-mode-detected? db)
             {:minimizable? true})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/add-popup!
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ;  {:autopadding? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:layout :boxed}
  ;   :body (metamorphic-content)
  ;   :destructor (metamorphic-event)(opt)
  ;   :header (metamorphic-content)(opt)
  ;   :hide-animated? (boolean)(opt)
  ;    Default: true
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :initializer (metamorphic-event)(opt)
  ;   :layout (keyword)(opt)
  ;    :boxed, :unboxed, :flip
  ;    Default: :boxed
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :render-exclusive? (boolean)(opt)
  ;    Default: false
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: true
  ;   :stretch-orientation (keyword)(opt)
  ;    :horizontal, :vertical, :both, :none
  ;    Default: :none
  ;    Only w/ {:layout :boxed}
  ;   :update-animated? (boolean)(opt)
  ;    Default: false
  ;   :user-close? (boolean)(opt)
  ;    A popup felület bezárható a surface cover felületre kattintva
  ;    Default: true}
  ;
  ; @usage
  ;  [:ui/add-popup! {...}]
  ;
  ; @usage
  ;  [:ui/add-popup! :my-popup {...}]
  ;
  ; @usage
  ;  (defn my-body   [popup-id]              [:div "My body"])
  ;  (defn my-header [popup-id header-props] [:div "My header"])
  ;  [:ui/add-popup! {:body   {:content #'my-body}
  ;                   :header {:content #'my-header :subscriber [:get-my-header-props]}}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ popup-id popup-props]]
      (let [popup-props (r popup-props-prototype db popup-id popup-props)]
           (if-let [render-exclusive? (get popup-props :render-exclusive?)]
                   [:ui/render-popup-exclusive! popup-id popup-props]
                   [:ui/render-popup!           popup-id popup-props]))))

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
  :ui/render-popup!
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
             {:ms  render-popup-delay :dispatch [:ui/render-popup! popup-id popup-props]}]})))