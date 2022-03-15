
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-prohibitor.side-effects
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.candy  :refer [param]]
              [mid-fruits.css    :as css]
              [mid-fruits.math   :as math]
              [mid-fruits.string :as string]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enable-dom-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [body-top (dom/get-body-style-value "top")
        scroll-y (-> body-top string/to-integer math/positive)]
       ; Engedélyezi a html elemen való görgetést
       (dom/remove-element-style-value! (dom/get-document-element) "overflow-y")
       ; Engedélyezi a body elemen való görgetést
       (dom/remove-element-style!       (dom/get-body-element))
       ; A body elem {:position ...} tulajdonságának visszaállítása miatt
       ; szükséges a scroll-y értékét újra beállítani
       (dom/set-scroll-y! scroll-y)))

(defn disable-dom-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [scroll-y (dom/get-scroll-y)
        body-top (math/negative scroll-y)
        ; A body elemen való görgetés letiltása, annak {:position "fixed"}
        ; tulajdonságának beállításával történik.
        ; A {:position "fixed"} tulajdonság beállítása miatt szükséges
        ; a {:width "100%"} tulajdonság beállítása is.
        ; A {:position "fixed"} tulajdonság beállítása miatt szükséges
        ; a body elemet BTT irányban eltolni (a tiltás előtti scroll-y értékkel)
        body-style {:position (param  "fixed")
                    :top      (css/px body-top)
                    :width    (param  "100%")}]
       ; Letiltja a html elemen való görgetést
       (dom/set-element-style-value! (dom/get-document-element) "overflow-y" "hidden")
       ; Letiltja a body elemen való görgetést
       (dom/set-element-style! (dom/get-body-element) body-style)))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
;
; Az [:environment/enable-dom-scroll! ...] mellékhatás esemény működését nem lehetséges
; Re-Frame esemény alapon megvalósítani, mert fontos, hogy a scroll érték
; beállítása Ca. 0ms különbséggel a body elem {:position "..."}
; tulajdonságának átállítása után történjen!
(a/reg-fx :environment/enable-dom-scroll! enable-dom-scroll!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/disable-dom-scroll! disable-dom-scroll!)
