
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.29
; Description:
; Version: v1.7.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups
    (:require [mid-fruits.candy       :refer [param return]]
              [x.app-core.api         :as a :refer [r]]
              [x.app-environment.api  :as environment]
              [x.app-ui.popup-layouts :rename {view popup-element}]
              [x.app-ui.renderer      :as renderer :rename {component renderer}]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name stretched?
;  {:stretched? true}
;  A popup felületén megjelenő tartalmat magában foglaló DIV elem
;  az app-popups teljes felületére kifeszül
;
;  {:stretched? false}
;  A popup felületén megjelenő tartalmat magában foglaló DIV elem
;  az app-popups közepére van pozícionálva, mérete a benne található
;  tartalom méretével megegyezik
;
; @name layout
;  {:layout :boxed}
;  A popup felületén megjelenő tartalmat magában foglaló DIV elem
;  háttere az applikáció témája szerinti háttérszín
;
; {:layout :unboxed}
; TODO ...
;
; @name render-exclusive?
;  A popup elem renderelése előtt bezárja az összes látható popup elemet és
;  látható surface elemet.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-props->render-popup-exclusive?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  ;  {:render-exclusive? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [render-exclusive?]}]
  (boolean render-exclusive?))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stretch-popup-anyway?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#3806
  ;  XXS, XS, S viewport-on a {:stretched? false :layout :boxed} popup felületek
  ;  {:stretched? true} popup felületként jelennek meg.
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:layout (keyword)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [layout]}]]
  (and (r environment/viewport-small? db)
       (not= layout :unboxed)))

(defn- get-upper-popup-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (r renderer/get-upper-visible-element-id db :popups))



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
  ;   :stretched? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [db [_ popup-id popup-props]]
  (merge ; 1. popup-props prototype
         {:autopadding?     true
          :hide-animated?   true
          :horizontal-align :center
          :layout           :boxed
          :min-width        :m
          :reveal-animated? true
          :stretched?       false
          :update-animated? false
          :user-close?      true}
         ; 2. popup-props
         (param popup-props)
         ; 3. Forced props
             ; XXX#3806
         (if (r stretch-popup-anyway? db popup-id popup-props)
             {:stretched? true})

         ; DEBUG
         (if (a/debug-mode?)
             {:minimizable? true})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- minimize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? true))

(a/reg-event-db :ui/minimize-popup! minimize-popup!)

(defn- maximize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? false))

(a/reg-event-db :ui/maximize-popup! maximize-popup!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/update-popup!
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @usage
  ;  [:ui/update-popup! :my-popup {...}]
  (fn [_ [_ popup-id popup-props]]
      [:ui/update-element! :popups popup-id popup-props]))

(a/reg-event-fx
  :ui/close-popup!
  ; @param (keyword) popup-id
  (fn [{:keys [db]} [_ popup-id]]
      {:dispatch-n [[:ui/destroy-element! :popups popup-id]
                    ; Eltávolítja a popup-id azonosítójú popup felület által elhelyezett scroll-tiltást
                    [:environment/remove-scroll-prohibition! popup-id]]}))

(a/reg-event-fx
  :ui/close-upper-popup!
  (fn [{:keys [db]} _]
      (if-let [upper-popup-id (r get-upper-popup-id db)]
              [:ui/close-popup! upper-popup-id])))

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
            close-header-duration  (r renderer/get-visible-elements-destroying-duration db :header)
            close-surface-delay    (param close-popups-duration)
            close-header-delay     (+ close-surface-delay close-surface-duration)
            render-popup-delay     (+ close-popups-duration close-surface-duration close-header-duration)]
           {:dispatch-later
            [{:ms                   0 :dispatch [:ui/destroy-all-elements! :popups]}
             {:ms close-surface-delay :dispatch [:ui/destroy-all-elements! :surface]}
             {:ms close-header-delay  :dispatch [:ui/destroy-all-elements! :header]}
             {:ms render-popup-delay  :dispatch [:ui/render-popup! popup-id popup-props]}]})))

(a/reg-event-fx
  :ui/add-popup!
  ; @param (keyword)(opt) popup-id
  ; @param (map) popup-props
  ;  {:autopadding? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:layout :boxed}
  ;   :body (map)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription-vector)(opt)}
  ;   :destructor (metamorphic-event)(opt)
  ;   :header (map)(opt)
  ;    {:content (metamorphic-content)
  ;      #'ui/go-back-popup-header, #'ui/close-popup-header, ...
  ;     :content-props (map)(opt)
  ;      {:label (metamorphic-content)(opt)}
  ;     :subscriber (subscription-vector)(opt)}
  ;   :hide-animated? (boolean)(opt)
  ;    Default: true
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :initializer (metamorphic-event)(opt)
  ;   :layout (keyword)(opt)
  ;    :boxed, :unboxed
  ;    Default: :boxed
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :render-exclusive? (boolean)(opt)
  ;    Default: false
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: true
  ;   :stretched? (boolean)(opt)
  ;    Default: false
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
  ;  (defn body   [popup-id]              [:div "My body"])
  ;  (defn header [popup-id header-props] [:div "My header"])
  ;  [:ui/add-popup! {:body   {:content #'body}
  ;                   :header {:content #'header :subscriber [::get-header-props]}}]
  (fn [{:keys [db]} event-vector]
      (let [popup-id    (a/event-vector->second-id   event-vector)
            popup-props (a/event-vector->first-props event-vector)
            popup-props (r popup-props-prototype db popup-id popup-props)]
           (if (popup-props->render-popup-exclusive? popup-props)
               [:ui/render-popup-exclusive! popup-id popup-props]
               [:ui/render-popup!           popup-id popup-props]))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :popups {:alternate-renderer-id :surface
                     :element               #'popup-element
                     :max-elements-rendered 5
                     :required?             true
                     :queue-behavior        :push
                     :rerender-same?        false}])
