
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.effects
    (:require [mid-fruits.candy        :refer [param]]
              [x.app-core.api          :as a :refer [r]]
              [x.app-ui.bubbles.engine :as bubbles.engine]
              [x.app-ui.bubbles.subs   :as bubbles.subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubble-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ;  {:autopop? (boolean)
  ;   :hide-animated? (boolean)
  ;   :initializer (metamorphic-event)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [bubble-id bubble-props]
  (merge {:autopop?    true
          :initializer [:ui/initialize-bubble! bubble-id]
          :user-close? true}
         (param bubble-props)
         {:hide-animated?   true
          :reveal-animated? true
          :update-animated? true}))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/initialize-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      {:dispatch-later [(if (r bubbles.subs/autopop-bubble? db bubble-id)
                            {:ms bubbles.engine/BUBBLE-LIFETIME :dispatch [:ui/autopop-bubble?! bubble-id]})]}))

(a/reg-event-fx
  :ui/autopop-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
            ; Ha a bubble rendelkezik {:autopop? true} tulajdonsággal és lejárt az ideje,
            ; akkor megtörténik a pop esemény.
      (cond (and (r bubbles.subs/autopop-bubble?          db bubble-id)
                 (r bubbles.subs/bubble-lifetime-elapsed? db bubble-id))
            [:ui/pop-bubble! bubble-id]
            ; Ha a bubble rendelkezik {:autopop? true} tulajdonsággal és még NEM járt le az ideje,
            ; akkor beidőzít egy autopop eseményt a bubble várható élettartamára.
            ; Előfordulhat, hogy a bubble hátralévő idejében újra meghívásra kerül a blow esemény,
            ; ami miatt újraindul a bubble élettartama.
            (r bubbles.subs/autopop-bubble? db bubble-id)
            {:dispatch-later [{:ms (r bubbles.subs/get-bubble-lifetime-left db bubble-id)
                               :dispatch [:ui/autopop-bubble?! bubble-id]}]})))

(a/reg-event-fx
  :ui/pop-bubble!
  ; @param (keyword) bubble-id
  ; @param (map)(opt) action-props
  ;  {:timeout (ms)(opt)
  ;    Default: 0}
  (fn [{:keys [db]} [_ bubble-id {:keys [timeout]}]]
      (if timeout {:dispatch-later [{:ms timeout :dispatch [:ui/destroy-element! :bubbles bubble-id]}]}
                  [:ui/destroy-element! :bubbles bubble-id])))

(a/reg-event-fx
  :ui/blow-bubble!
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ;  {:autopop? (boolean)(opt)
  ;    Default: true
  ;   :body (metamorphic-content)
  ;   :destructor (metamorphic-event)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :user-close? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:ui/blow-bubble! {...}]
  ;
  ; @usage
  ;  [:ui/blow-bubble! :my-bubble {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ bubble-id bubble-props]]
      (let [bubble-props (bubble-props-prototype bubble-id bubble-props)]
           {:dispatch-if [(r bubbles.subs/bubbles-enabled-by-user? db)
                          [:ui/request-rendering-element! :bubbles bubble-id bubble-props]]})))
