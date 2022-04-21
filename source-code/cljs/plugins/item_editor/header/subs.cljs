
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.subs
    (:require [mid-fruits.candy                   :refer [return]]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.header.subs :as header.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.header.subs
(def get-header-prop      header.subs/get-header-prop)
(def header-props-stored? header.subs/header-props-stored?)
(def header-did-mount?    header.subs/header-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (keyword)
  [db [_ editor-id]])
  ; - A get-selected-view-id függvény visszatérési értéke:
  ;   1. az [:item-editor/change-view! ...] esemény használatával beállított {:view-id ...} tulajdonság.
  ;   2. a header komponens {:menu-items [...]} paraméterének első elemének {:view-id ...} tulajdonsága.
  ;   3. a DEFAULT-VIEW-ID konstans értéke.
  ;
  ; - BUG#0041
  ;   A DEFAULT-VIEW-ID konstans értékével való visszatérésre akkor van szükség, amikor
  ;   a body komponens hamarabb a React-fába csatolódik, mint a header komponens, ezért a body
  ;   komponens már azelőtt feliratkozik az [:item-editor/get-selected-view-id ...] értékére,
  ;   mielőtt a header komponens a Re-Frame adatbázisba írná a paramétereit.
  ;   A body komponenseken használt (case selected-view-id ...) függvényeknek szüksége van
  ;   a selected-view-id értékére, hogy ne kelljen a (case ...) függvény default ágát használni
  ;   minden body komponensnél.
  ;(or (r core.subs/get-meta-item db editor-id :view-id)))
      ;(get-in (r mount.subs/get-header-prop db editor-id :menu-items)
      ;        [0 :view-id])
      ;(return header.config/DEFAULT-VIEW-ID)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/header-props-stored? header-props-stored?)
