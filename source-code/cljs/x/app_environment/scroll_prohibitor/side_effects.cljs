
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-prohibitor.side-effects
    (:require [css.api                                     :as css]
              [dom.api                                     :as dom]
              [mid-fruits.candy                            :refer [param]]
              [math.api                                    :as math]
              [mid-fruits.string                           :as string]
              [re-frame.api                                :as r]
              [x.app-environment.scroll-prohibitor.helpers :as scroll-prohibitor.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enable-dom-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; A görgetés engedélyezése előtt szükséges vizsgálni a letiltás állapotát,
  ; mert ha nem volt letiltva a görgetés a visszaállítás előtt, akkor az enable-dom-scroll!
  ; függvény hatására feleslegesen alaphelyzetbe állítódna a scroll pozíció (0px)!
  (if (scroll-prohibitor.helpers/dom-scroll-disabled?)
      (let [body-top (dom/get-body-style-value "top")
            scroll-y (-> body-top string/to-integer math/positive)]
           ; Engedélyezi a html elemen való görgetést
           (dom/remove-element-style-value! (dom/get-document-element) "overflow-y")
           ; Engedélyezi a body elemen való görgetést
           (dom/remove-element-style!       (dom/get-body-element))
           ; A body elem {:position ...} tulajdonságának visszaállítása miatt
           ; szükséges a scroll-y értékét újra beállítani
           (dom/set-scroll-y! scroll-y)
           ; Eltávolítja a jelölést a body elemről
           (dom/remove-element-attribute!   (dom/get-document-element) "data-scroll-disabled"))))

(defn disable-dom-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [scroll-y (dom/get-scroll-y)
        body-top (math/negative scroll-y)
        ; A body elemen való görgetés letiltása, annak {:position "fixed"}
        ; tulajdonságának beállításával történik.
        ; A {:position "fixed"} tulajdonság beállítása miatt szükséges ...
        ; ... a {:width "100%"} tulajdonság beállítása is.
        ; ... a body elemet BTT irányban eltolni (a tiltás előtti scroll-y értékkel)
        body-style {:position (param  "fixed")
                    :top      (css/px body-top)
                    :width    (param  "100%")}]
       ; Letiltja a html elemen való görgetést
       (dom/set-element-style-value! (dom/get-document-element) "overflow-y" "hidden")
       ; Letiltja a body elemen való görgetést
       (dom/set-element-style!       (dom/get-body-element) body-style)
       ; Megjelöli a body elemet
       (dom/set-element-attribute!   (dom/get-document-element) "data-scroll-disabled" "true")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
;
; To avoid flickering:
; Az [:environment/enable-dom-scroll! ...] mellékhatás esemény működését nem lehetséges
; Re-Frame esemény alapon megvalósítani, mert fontos, hogy a scroll érték
; beállítása Ca. 0ms különbséggel a body elem {:position "..."}
; tulajdonságának átállítása után történjen!
(r/reg-fx :environment/enable-dom-scroll! enable-dom-scroll!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/disable-dom-scroll! disable-dom-scroll!)
