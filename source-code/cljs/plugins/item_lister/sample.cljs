
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

; A plugin használatához mindenképpen szükséges a szerver-oldali [:item-lister/init-lister! ...]
; eseményt alkalmazni!



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



;; -- Listaelemek szűrése eseménnyel ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/use-filter! ...] esemény használatával lehetséges szűrési feltételeket beállítani
(a/reg-event-fx
  :use-my-filter!
  [:item-lister/use-filter! :my-lister {}])



;; -- Szűrők használata -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  []
  (let [my-filter-event [:item-lister/use-filter! :my-lister {}]]
       [elements/menu-bar {:menu-items [{:label "My filter" :on-click my-filter-event}]}]))

(defn my-view-with-filters
  [surface-id]
  [:<> [my-filters]
       [item-lister/header :my-lister {}]
       [item-lister/body   :my-lister {}]])



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  [surface-id]
  [item-lister/body :my-lister {:list-element [:div "My item"]
                                :prefilter    {:namespace/color "red"}}])



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az adatok kliens-oldalon megjelenített változatát aktualizálni
; a szerver-oldali változathoz.
(a/reg-event-fx
  :reload-my-items!
  [:item-lister/reload-items! :my-lister])



;; -- Több listaelem kijelölése a SHIFT billentyű használatával ---------------
;; ----------------------------------------------------------------------------

(defn my-selectable-list-element
  [lister-id item-dex item]
  [elements/toggle {:content  [:div "My item"]
                    :on-click [:my-list-element-clicked lister-id item-dex item]}])

; Ha a listaelemek kijelölhetők és a lista-elemre kattintás pillanatában a SHIFT billenytű
; lenyomott állapotban volt, akkor a toggle-item-selection? függvény visszatérési értéke TRUE
(a/reg-event-fx
  :my-list-element-clicked
  (fn [{:keys [db]} [_ lister-id item-dex item]]
      (if (r item-lister/toggle-item-selection? db lister-id item-dex)
          {:db (r item-lister/toggle-item-selection! db lister-id item-dex)}
          [:my-event])))



;; -- Kontextus-menü használata a listaelemeken -------------------------------
;; ----------------------------------------------------------------------------

(defn my-contextual-list-element
  [lister-id item-dex item]
  [elements/toggle {:content        [:div "My item"]
                    :on-click       [:my-event]
                    :on-right-click [:render-my-context-menu!]}])

(a/reg-event-fx
  :render-my-context-menu!
  [:ui/add-popup! :my-context-menu {:body [:div "My context menu"]}])



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  [surface-id]
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
; - Az item-lister plugin [:item-lister/toggle-*-mode! ...] események használatával
;  tudsz a különbözű menü módok között váltani (több elem kiválasztása mód, rendezés mód, stb.)
(defn my-menu-element
  [lister-id]
  [elements/row {:content [item-lister/add-new-item-button  lister-id]
                          [item-lister/toggle-select-button lister-id]}])

(defn my-header-with-my-menu
  [surface-id]
  [item-lister/header :my-lister {:menu-element #'my-menu-element}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; Ha az order-by-options beállításban egyedi értékeket is használtál, akkor ne felejtsd el
; hozzáadni a megfelelő szótári szavakat!
; Pl. a :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:by-my-order-ascending {:en "..." :hu "..."}}]})
