
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.sample
    (:require [plugins.item-editor.api :as item-editor]
              [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]))



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
      (case selected-option :add-my-type!   [:do-something!]
                            :add-your-type! [:do-something-else!])))



;; -- Listaelemek szűrése eseménnyel ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/use-filter! ...] esemény használatával lehetséges szűrési feltételeket beállítani
(a/reg-event-fx
  :use-my-filter!
  (fn [_ [_ filter-pattern]]
      [:item-lister/use-filter! :my-extension :my-type filter-pattern]))



;; -- Szűrők használata -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  []
  (let [my-filter-event [:my-extension.my-type-lister/use-filter! :my-extension :my-type {}]]
       [elements/menu-bar {:menu-items [{:label "My filter" :on-click my-filter-event}]}]))

(defn my-view-with-filters
  [surface-id]
  [:<> [my-filters]
       [item-lister/header :my-extension :my-type {}]
       [item-lister/body   :my-extension :my-type {}]])



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; a szerver-oldali kollekció elemeiből csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  [surface-id]
  [item-lister/body :my-extension :my-type {:list-element [:div "My item"]
                                            :prefilter    {:my-type/color "red"}}])



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az szerveren tárolt adatokat aktualizálni a kliens-oldalon.
(a/reg-event-fx
  :reload-my-items!
  [:item-lister/reload-items! :my-extension :my-type])



;; -- Több listaelem kijelölése a SHIFT billentyű használatával ---------------
;; ----------------------------------------------------------------------------

(defn my-selectable-list-element
  [extension-id item-namespace item-dex item]
  [elements/toggle {:content  [:div "My item"]
                    :on-click [:my-list-element-clicked extension-id item-namespace item-dex item]}])

; Ha a listaelemek kijelölhetők és a kattintás pillanatában a SHIFT billenytű lenyomott
; állapotban volt, akkor a toggle-item-selection? függvény visszatérési értéke TRUE
(a/reg-event-fx
  :my-list-element-clicked
  (fn [{:keys [db]} [_ extension-id item-namespace item-dex item]]
      (if (r item-lister/toggle-item-selection? db extension-id item-namespace item-dex)
          {:db (r item-lister/toggle-item-selection! db extension-id item-namespace item-dex)}
          [:do-something!])))



;; -- Kontextus-menü használata a listaelemeken -------------------------------
;; ----------------------------------------------------------------------------

(defn my-contextual-list-element
  [extension-id item-namespace item-dex item]
  [elements/toggle {:content        [:div "My item"]
                    :on-click       [:do-something!]
                    :on-right-click [:render-my-context-menu!]}])

(a/reg-event-fx
  :render-my-context-menu!
  [:ui/add-popup! :my-context-menu {:body [:div "My context menu"]}])



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  [surface-id]
  [:<> [item-lister/header :my-extension :my-type {}]
       [item-lister/body   :my-extension :my-type {:list-element [:div "My item"]}]])



;; -- Plugin használata "Layout A" felületeen ---------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :my-extension :my-type])]
       [layouts/layout-a surface-id {:header [item-lister/header :my-extension :my-type {}]
                                     :body   [item-lister/body   :my-extension :my-type {:list-element   [:div "My item"]}]
                                     :description description}]))



;; -- Egyedi menü használata --------------------------------------------------
;; ----------------------------------------------------------------------------

; - A header komponens számára átadott {:menu-element #'...} tulajdonság beállításával lehetséges
;   egyedi menüt használni.
; - Az item-lister plugin [:item-lister/toggle-*-mode! ...] események használatával
;  tudsz a különbözű módok között váltani (több elem kiválasztása mód, rendezés mód, stb.)
(defn my-menu-element
  [extension-id item-namespace]
  [elements/row {:content [item-lister/add-new-item-button  extension-id item-namespace]
                          [item-lister/toggle-select-button extension-id item-namespace]}])

(defn my-header-with-my-menu
  [surface-id]
  [item-lister/header :my-extension :my-type {:menu-element #'my-menu-element}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; Ha az order-by-options beállításban használtál egyedi értékeket, akkor ne felejtsd el
; hozzáadni a megfelelő szótári szavakat!
; Pl. a :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:by-my-order-ascending {:en "..." :hu "..."}}]})
