
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.sample
    (:require [plugins.item-editor.api :as item-editor]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-editor/init-editor! ...] eseményt használni!



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  []
  [:<> [item-editor/body   :my-editor {:form-element [:div "My form"]}]
       [item-editor/footer :my-editor {}]])



;; -- A plugin használata "Layout A" felületen --------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-editor/get-description :your-editor])]
       [layouts/layout-a surface-id {:body   [item-editor/body   :your-editor {:form-element [:div "Your form"]}]
                                     :footer [item-editor/footer :your-editor {}]
                                     :description description}]))



;; -- A :form-id tulajdonság használata ---------------------------------------
;; ----------------------------------------------------------------------------

; Az item-editor plugin "Mentés" gombja mindaddig {:disabled? true} állapotban marad, amíg
; a megjelenített inputok közül nincs kitöltve az összes olyan, amelyik ...
; ... {:required? true} beállítással rendelkezik.
; ... a {:form-id ...} tulajdonsága megyegyezik az item-editor body komponensének {:form-id ...}
;     tulajdonságával.
; (Fejlesztői módban elindítitott applikáció esetén a funkció inaktív!)
(defn our-type-form
  [editor-id]
  [elements/text-field ::our-sample-field
                       {:form-id    :my-form
                        :required?  true
                        :value-path [:our-editor :our-field]}])

(defn our-view
  []
  [item-editor/view :our-editor {:form-element #'our-type-form
                                 :form-id :my-form}])



;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------

; Ha az item-editor plugin {:auto-title? true} beállítással van használva, akkor a szerkesztő betöltésekor
; a plugin a megfelelő kifejezést beállítja az applikáció címének, ezért szükséges azokat hozzáadni a szótárhoz!
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:add-my-type  {:en "Add my type"  :hu "Típusom hozzáadása"}
                                         :edit-my-type {:en "Edit my type" :hu "Típusom szerkesztése"}}]})



;; -- Az item-label komponens/feliratkozás használata -------------------------
;; ----------------------------------------------------------------------------

; ...
(defn my-item-label
  []
  (let [my-item-name @(a/subscribe [:db/get-item [:my-editor :name]])]
       [item-editor/item-label {:name my-item-name}]))

; ...
(defn your-item-label
  []
  (let [your-item-name  @(a/subscribe [:db/get-item [:your-editor :name]])
        your-item-label @(a/subscribe [:item-editor/get-current-item-label :your-editor your-item-name])]
       [:div your-item-label]))

; Az item-editor plugin item-label funkciójának használatakor szükséges a szótárhoz adni
; a megfelelő kifejezéseket!
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:new-my-type     {:en "New my type"     :hu "Új típusom"}
                                         :unnamed-my-type {:en "Unnamed my type" :hu "Névtelen típusom"}}]})



;; -- Az {:initial-item {...}} tulajdonság használata -------------------------
;; ----------------------------------------------------------------------------

; - Az item-editor plugin számára átadott form-element komponensben ne használj olyan input mezőt,
;   ami {:initial-value ...} tulajdonsággal rendelkezik, mert ...
;   Pl.: Új elem létrehozásakor az input mezők {:initial-value ...} értékei megváltoztatják a dokumentumot,
;        és ha a felhasználó a dokumentum változtatása nélkül elhagyja a szerkesztőt, akkor az tévesen
;        úgy érzékelné, hogy a dokumentumot a felhasználó változtatta meg és az elhagyás után felajánlaná
;        a "Nem mentett változtatások visszaállítása" lehetőségét!
;
; - ...



;; -- Az [:item-editor/edit-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(a/reg-event-fx
  :edit-my-item!
  [:item-editor/edit-item! :my-editor "my-item"])
