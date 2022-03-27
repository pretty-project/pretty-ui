
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

; A plugin beállításához mindenképpen szükséges a szerver-oldali [:item-editor/init-editor! ...]
; eseményt használni!



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  []
  [:<> [item-editor/header :my-editor {}]
       [item-editor/body   :my-editor {:form-element [:div "My form"]}]])



;; -- A plugin használata "Layout A" felületen --------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-editor/get-description :your-editor])]
       [layouts/layout-a surface-id {:header [item-editor/header :your-editor {}]
                                     :body   [item-editor/body   :your-editor {:form-element [:div "Your form"]}]
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



;; -- Az {:initial-item {...}} tulajdonság használata -------------------------
;; ----------------------------------------------------------------------------

; - Az item-editor plugin számára átadott form-element komponensben ne használj olyan input mezőt,
;   ami {:initial-value ...} tulajdonsággal rendelkezik, mert ...
;   Pl.: Új elem létrehozásakor az input mezők {:initial-value ...} értékei megváltoztatják a dokumentumot,
;        és ha a felhasználó a dokumentum változtatása nélkül elhagyja a szerkesztőt, akkor az tévesen
;        úgy érzékelné, hogy a dokumentumot a felhasználó változtatta meg és az elhagyás után felajánlaná
;        a "Nem mentett változtatások visszaállítása" lehetőségét!
; - ...



;; -- Az [:item-editor/edit-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(a/reg-event-fx
  :edit-my-item!
  [:item-editor/edit-item! :my-editor "my-item"])
