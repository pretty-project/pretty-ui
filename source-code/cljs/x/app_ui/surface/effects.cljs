
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.effects
    (:require [mid-fruits.candy       :refer [param]]
              [x.app-core.api         :as a :refer [r]]
              [x.app-ui.header.events :as header.events]
              [x.app-ui.renderer      :as renderer]))



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
  ;   :horizontal-align (keyword)
  ;   :reveal-animated? (boolean)
  ;   :trim-content? (boolean)
  ;   :update-animated? (boolean)}
  [surface-props]
  (merge {:hide-animated?   false
          :reveal-animated? false
          :trim-content?    false
          :update-animated? false
          :horizontal-align :center}
         (param surface-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/close-surface!
  ; @param (keyword) surface-id
  (fn [{:keys [db]} [_ surface-id]]
      [:ui/destroy-element! :surface surface-id]))

(a/reg-event-fx
  :ui/render-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:render-animated? (boolean)(opt)}
  (fn [{:keys [db]} [_ surface-id {:keys [render-animated?] :as surface-props}]]
      (let [close-popups-duration (r renderer/get-visible-elements-destroying-duration db :popups)]
           {:dispatch-later
            [{:ms                     0 :dispatch [:ui/destroy-all-elements! :popups]}
             {:ms close-popups-duration :dispatch [:environment/enable-scroll!]}
             {:ms close-popups-duration :dispatch [:ui/request-rendering-element! :surface surface-id surface-props]}]})))

(a/reg-event-fx
  :ui/set-surface!
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:destructor (metamorphic-event)(opt)
  ;   :hide-animated? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :initializer (metamorphic-event)(opt)
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: false
  ;   :route-parent (string)(opt)
  ;   :title (metamorphic-content)(opt)
  ;   :trim-content? (boolean)(opt)
  ;    A surface felületéről az X tengelyen túlméretes tartalom elrejtése.
  ;    Default: false
  ;    BUG#9330
  ;    A surface felületén megjelenített {position: sticky;} tulajdonságú
  ;    tartalmak pozícionálása nem kompatibilis a {:trim-content? true}
  ;    tulajdonság használatával!
  ;   :update-animated? (boolean)(opt)
  ;    Default: false
  ;   :view (metamorphic-content)
  ;
  ; @usage
  ;  [:ui/set-surface! {...}]
  ;
  ; @usage
  ;  [:ui/set-surface! :my-surface {...}]
  ;
  ; @usage
  ;  (defn my-view [surface-id] [:div "My surface"])
  ;  [:ui/set-surface! {:view {:content #'my-view}}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ surface-id {:keys [route-parent title] :as surface-props}]]
      (let [surface-props (surface-props-prototype surface-props)]
           {:db (as-> db % (if-not title        % (r header.events/set-header-title! % title))
                           (if-not route-parent % (r header.events/set-route-parent! % route-parent)))
            :dispatch-n [(if title [:ui/set-window-title! title])
                         [:ui/render-surface! surface-id surface-props]]})))