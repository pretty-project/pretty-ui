
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.sample
    (:require [plugins.item-editor.api :as item-editor]
              [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]))




;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-lister/init-lister! ...] eseményt használni!



;; -- "Új elem létrehozása" esemény használata  -------------------------------
;; ----------------------------------------------------------------------------

; Az item-lister/header komponens {:new-item-event [...]} tulajdonságának használatával
; a komponens menü elemei között megjelenik az "Új elem hozzáadása" gomb, aminek megnyomására
; megtörténik a {:new-item-event [...]} tulajdonságként átadott esemény.



;; -- "Új elem létrehozása" opciók használata  --------------------------------
;; ----------------------------------------------------------------------------

; - Az item-lister/header komponens {:new-item-options [...]} tulajdonságának használatával
;   a komponens menü elemei között megjelenített "Új elem hozzáadása" gomb egy párbeszédablakot
;   nyit meg a {:new-item-options [...]} tulajdonságként átadott vektor elemeivel
; - Az egyes elemek kiválasztásakor megtörténik a {:new-item-event [...]} tulajdonságként átadott
;   esemény, ami utolsó paraméterként megkapja a kiválasztott értéket.
(a/reg-event-fx
  :add-my-new-item!
  (fn [_ [_ selected-option]]
      (case selected-option :add-my-item!   [:my-event]
                            :add-your-item! [:your-event])))



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  []
  [item-lister/body :my-lister {:list-element [:div "My item"]
                                :prefilter    {:my-type/color "red"}}])



;; -- Listaelemek szűrése eseménnyel ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/filter-items! ...] esemény használatával lehetséges szűrési feltételeket beállítani
(a/reg-event-fx
  :use-my-filter!
  [:item-lister/filter-items! :my-lister {:$or [{:my-type/id "my-item"} {:your-type/id "your-item"}]}])



;; -- Szűrők használata -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  []
  (let [my-filter-event [:item-lister/filter-items! :my-lister {}]]
       [elements/menu-bar {:menu-items [{:label "My filter" :on-click my-filter-event}]}]))

(defn my-view-with-filters
  []
  [:<> [my-filters]
       [item-lister/header :my-lister {}]
       [item-lister/body   :my-lister {}]])



;; -- Előre kijelölt elemek ---------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-lister/body komponensének {:selected-items [...]} paraméterként lehetséges
; azon elemek azonosítóit átadni, amely elemeket szeretnéd, ha a letöltődésük után
; ki lennének jelölve.



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az adatok kliens-oldalon megjelenített változatát aktualizálni
; a szerver-oldali változathoz.
(a/reg-event-fx
  :reload-my-items!
  [:item-lister/reload-items! :my-lister])



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  []
  [:<> [item-lister/header :my-lister {}]
       [item-lister/body   :my-lister {:list-element [:div "My item"]}]])



;; -- Plugin használata "Layout A" felületeen ---------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :my-lister])]
       [layouts/layout-a surface-id {:header [item-lister/header :my-lister {}]
                                     :body   [item-lister/body   :my-lister {:list-element [:div "My item"]}]
                                     :description description}]))



;; -- Egyedi menü használata --------------------------------------------------
;; ----------------------------------------------------------------------------

; - A header komponens számára átadott {:menu-element #'...} tulajdonság beállításával lehetséges
;   egyedi menüt használni.
; - Az item-lister plugin [:item-lister/set-*-mode! ...] események használatával
;  tudsz a különbözű menü módok között váltani (több elem kiválasztása mód, rendezés mód, stb.)
(defn my-menu-element
  [lister-id]
  [elements/row {:content [item-lister/new-item-block lister-id]
                          [item-lister/search-block   lister-id]}])

(defn my-header-with-my-menu
  []
  [item-lister/header :my-lister {:menu-element #'my-menu-element}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; Ha az order-by-options beállításban egyedi értékeket is használtál, akkor ne felejtsd el
; hozzáadni a megfelelő szótári szavakat!
; Pl. a :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:by-my-order-ascending {:en "..." :hu "..."}}]})
