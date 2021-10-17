
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v1.8.0
; Compatibility: x4.2.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-prohibitor
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.candy  :refer [param]]
              [mid-fruits.css    :as css]
              [mid-fruits.map    :as map]
              [mid-fruits.math   :as math]
              [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name scroll-prohibitor
;  Letiltja a görgetést. A tiltások saját azonosítóval rendelkeznek
;  es egyszerre több tiltás is lehet érvényben.



;; -- Scroll prohibitions -----------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#7650
; A következő beállítások teszik lehetővé az egyes böngészőkben
; a görgetés letiltását:
;
; Opera browser 76.0 (MacOS 10.15.7)
;  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]
;
; Mozilla Firefox 88.0 (MacOS 10.15.7)
;  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]
;
; Safari 13.1 (MacOS 10.15.7)
;  [:html {:style {:overflow "hidden"}}]
;
; Google Chrome 90.0 (MacOS 10.15.7)
;  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]
;
; Google Chrome for mobile 86.0 (iOS 14.3, iPhone 6s)
;  [:body {:style {:position "fixed"}}]
;  WARNING! Lehúzással nem lehetséges frissíteni az oldalt, ha a görgetés le van tiltva!
;
; Google Chrome for mobile 90.0 (Google Android ?, Samsung S8+)
;  TODO ...
;
; Safari ? (iOS 14.3, iPhone 6s)
;  TODO ...


; XXX#7659
; Az app inicializalasakor, amikor még látható a töltő képernyő,
; de az app-container rendereren mar megjelent olyan tartalom ami 100vh-nal
; magasabb akkor felvillan a scrollbar.
; Ennek elkerülése végett a html elemen, inline-style-ként legyen rögzítve
; az "overflow: hidden" tulajdonság.



; WARNING!
; Google Chrome for mobile 90.0 (Google Android ?, Samsung S8+)
;
; A @dnd-kit/sortable elemei hibásan érzékelik scroll-y értéket,
; ha a [:html {:style {:overflow-y "scroll"}}] érték be van állítva.
; A görgetés tiltásának feloldása után ezért az {:overflow-y "hidden"} értéket
; törölni szükséges, {:overflow-y "scroll"} értékre állítás helyett.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-scroll-prohibitions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::prohibitions)))

(defn scroll-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (map/nonempty? (r get-scroll-prohibitions db)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-scroll-prohibitions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/path ::prohibitions)
               (param {})))

(a/reg-event-db ::remove-scroll-prohibitions! remove-scroll-prohibitions!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  ; Az [::enable-scroll-effects!] mellékhatás esemény működését nem lehetséges
  ; Re-Frame esemény alapon megvalósítani, mert fontos, hogy a scroll érték
  ; beállítása Ca. 0ms különbséggel a body elem {:position "..."}
  ; tulajdonságának átállítása után történjen!
  ::enable-dom-scroll!
  #(let [body-top (dom/get-body-style-value "top")
         scroll-y (math/positive (string/to-integer body-top))]
        ; Engedélyezi a html elemen való görgetést
        (dom/remove-element-style-value! (dom/get-document-element)
                                         (param "overflow-y"))
        ; Engedélyezi a body elemen való görgetést
        (dom/remove-element-style!       (dom/get-body-element))
        ; A body elem {:position ...} tulajdonságának visszaállítása miatt
        ; szükséges a scroll-y értékét újra beállítani
        (dom/set-scroll-y!               (param scroll-y))))

(a/reg-handled-fx
  ::disable-dom-scroll!
  #(let [scroll-y (dom/get-scroll-y)
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
       (dom/set-element-style-value! (dom/get-document-element)
                                     (param "overflow-y")
                                     (param "hidden"))
       ; Letiltja a body elemen való görgetést
       (dom/set-element-style! (dom/get-body-element)
                               (param body-style))))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->scroll-prohibitions-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r scroll-disabled? db)
          [::disable-dom-scroll!]
          [::enable-dom-scroll!])))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::remove-scroll-prohibition!
  ; @param (keyword) prohibition-id
  (fn [_ [_ prohibition-id]]
      {:x.app-db [[:remove-item! (db/path ::prohibitions prohibition-id)]]
       :dispatch [::->scroll-prohibitions-changed]}))

(a/reg-event-fx
  ::add-scroll-prohibition!
  ; @param (keyword) prohibition-id
  (fn [_ [_ prohibition-id]]
      {:x.app-db [[:set-item! (db/path ::prohibitions prohibition-id)
                              (param {})]]
       :dispatch [::->scroll-prohibitions-changed]}))

(a/reg-event-fx
  ::enable-scroll!
  {:dispatch-n [[::remove-scroll-prohibitions!]
                [::->scroll-prohibitions-changed]]})
