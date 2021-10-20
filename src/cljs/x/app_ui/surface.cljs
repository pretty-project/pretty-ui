
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.8.0
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.keyword        :as keyword]
              [x.app-core.api            :as a :refer [r]]
              [x.app-ui.renderer         :as renderer :refer [view] :rename {view renderer}]
              [x.app-ui.sidebar          :as sidebar]
              [x.app-ui.surface-geometry :as geometry]
              [x.app-ui.surface-header   :refer [view] :rename {view surface-header}]
              [x.app-ui.surface-layouts  :refer [view] :rename {view surface-element}]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-id->header-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @example
  ;  (surface-id->header-id :my-surface/view)
  ;  => :my-surface/view--header
  ;
  ; @return (keyword)
  [surface-id]
  (keyword/append surface-id :header "--"))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)
  ;   :trim-content? (boolean)
  ;   :update-animated? (boolean)}
  [db [surface-id surface-props]]
  (merge {:hide-animated?   true
          :reveal-animated? true
          :trim-content?    false
          :update-animated? false}
         (param surface-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-surface-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:control-bar (map)
  ;   :label-bar (map)}
  ;
  ; @return (boolean)
  [db [_ surface-id {:keys [label-bar control-bar]}]]
  (let [stored-control-bar (r renderer/get-element-prop db :surface surface-id :control-bar)
        stored-label-bar   (r renderer/get-element-prop db :surface surface-id :label-bar)]
       (boolean (or (not= control-bar stored-control-bar)
                    (not= label-bar   stored-label-bar)))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/close-surface!
  ; @param (keyword) surface-id
  (fn [{:keys [db]} [_ surface-id]]
      [:x.app-ui/destroy-element! :surface surface-id]))

(a/reg-event-fx
  :x.app-ui/enable-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-environment.scroll-prohibitor/enable-scroll!])

(a/reg-event-fx
  :x.app-ui/set-surface-background!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]))

(a/reg-event-fx
  :x.app-ui/render-surface-header?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  (fn [{:keys [db]} [_ surface-id surface-props]]
      (if (r render-surface-header? db surface-id surface-props)
          [:x.app-ui/set-header! (surface-id->header-id surface-id)
                                 {:content          #'surface-header
                                  :content-props    surface-props
                                  :hide-animated?   true
                                  :reveal-animated? true
                                  :update-animated? true}])))

(a/reg-event-fx
  :x.app-ui/render-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:render-animated? (boolean)(opt)}
  (fn [{:keys [db]} [_ surface-id {:keys [render-animated?] :as surface-props}]]
      (let [close-popups-duration   (r renderer/get-visible-elements-destroying-duration db :popups)
            sidebar-hiding-duration (r sidebar/get-sidebar-hiding-duration db)
            surface-rendering-delay (+ close-popups-duration sidebar-hiding-duration)]
           {:dispatch-later
            [{:ms                       0 :dispatch [:x.app-ui/destroy-all-elements! :popups]}
             {:ms close-popups-duration   :dispatch [:x.app-ui/enable-scroll!]}
             {:ms close-popups-duration   :dispatch [:x.app-ui/hide-sidebar!]}
             {:ms surface-rendering-delay :dispatch [:x.app-ui/render-element! :surface surface-id surface-props]}
             {:ms surface-rendering-delay :dispatch [:x.app-ui/render-surface-header?!  surface-id surface-props]}]})))

(a/reg-event-fx
  :x.app-ui/set-surface!
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:content (component)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :control-bar (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}

  ;   WARNING! DEPRECATED!
  ;   :control-sidebar (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   WARNING! DEPRECATED!

  ;   WARNING! DEPRECATED!
  ;   :control-surface (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   WARNING! DEPRECATED!

  ;   :destructor (metamorphic-event)(opt)
  ;   :hide-animated? (boolean)(opt)
  ;    Default: true
  ;   :horizontal-align (keyword)(opt)
  ;    TODO ... (same as popup)
  ;   :initializer (metamorphic-event)(opt)
  ;   :label-bar (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;      {:label (metamorphic-content)(opt)}
  ;     :subscriber (subscription vector)(opt)}
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: true
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711
  ;   :trim-content? (boolean)(opt)
  ;    A surface felületéről az X tengelyen túlméretes tartalom elrejtése.
  ;    Default: false
  ;    BUG#9330
  ;    A surface felületén megjelenített {position: sticky;} tulajdonságú
  ;    tartalmak pozícionálása nem kompatibilis a {:trim-content? true}
  ;    tulajdonság használatával!
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.app-ui/set-surface! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-surface! :my-surface {...}]
  ;
  ; @usage
  ;  (defn view        [surface-id]            [:div "My surface"])
  ;  (defn control-bar [surface-id]            [:div "My control-bar"])
  ;  (defn label-bar   [surface-id view-props] [:div "My label-bar"])
  ;  [:x.app-ui/set-surface! {:content #'view
  ;                           :control-bar {:content #'control-bar}
  ;                           :label-bar   {:content #'label-bar
  ;                                         :subscriber [::get-label-bar-view-props]}}]
  (fn [{:keys [db]} event-vector]
      (let [surface-id    (a/event-vector->second-id   event-vector)
            surface-props (a/event-vector->first-props event-vector)
            surface-props (a/sub-prot db [surface-id surface-props] surface-props-prototype)]
           [:x.app-ui/render-surface! surface-id surface-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :surface {:element               #'surface-element
                      :max-elements-rendered 1
                      :queue-behavior        :push
                      :required?             true
                      :rerender-same?        false}])
