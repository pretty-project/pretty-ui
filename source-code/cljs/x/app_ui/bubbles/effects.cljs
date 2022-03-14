
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.effects
    (:require [x.app-core.api              :as a :refer [r]]
              [x.app-ui.bubbles.config     :as bubbles.config]
              [x.app-ui.bubbles.prototypes :as bubbles.prototypes]
              [x.app-ui.bubbles.subs       :as bubbles.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/initialize-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      {:dispatch-later [(if (r bubbles.subs/autopop-bubble? db bubble-id)
                            {:ms bubbles.config/BUBBLE-LIFETIME :dispatch [:ui/autopop-bubble?! bubble-id]})]}))

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
      (let [bubble-props (bubbles.prototypes/bubble-props-prototype bubble-id bubble-props)]
           {:dispatch-if [(r bubbles.subs/bubbles-enabled-by-user? db)
                          [:ui/request-rendering-element! :bubbles bubble-id bubble-props]]})))
