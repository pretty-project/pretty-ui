
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.core.events
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.map                    :refer [dissoc-in]]
              [plugins.value-editor.core.helpers :as core.helpers]
              [plugins.value-editor.core.subs    :as core.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A value-editor plugin minden elindulásakor kitörli a default-edit-path útvonalon található
  ; értéket, így az előző szerkesztésből esetlegesen megmaradt érték törlésre kerül.
  (let [default-edit-path (core.helpers/default-edit-path editor-id)]
       (dissoc-in db default-edit-path)))

(defn store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ editor-id editor-props]]
  (assoc-in db [:plugins :value-editor/editor-props editor-id] editor-props))

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (if-let [initial-value (r core.subs/get-editor-prop db editor-id :initial-value)]
          (let [edit-path (r core.subs/get-editor-prop db editor-id :edit-path)]
               (assoc-in db edit-path initial-value))
          (return db)))

(defn use-original-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; Ha nem az eredeti érték elérési útvonalán történik a szerkesztés, tehát az edit-path
  ; értéke nem egyenlő a value-path értékével és nincs alkalmazva initial-value érték,
  ; akkor a value-path útvonalon található érték lesz a szerkesztő mező kezdeti értéke.
  (if-not (or (r core.subs/get-editor-prop db editor-id :initial-value)
              (r core.subs/edit-original?  db editor-id))
          (if-let [original-value (r core.subs/get-original-value db editor-id)]
                  (let [edit-path (r core.subs/get-editor-prop    db editor-id :edit-path)]
                       (assoc-in db edit-path original-value))
                  (return db))
          (return db)))

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ editor-id editor-props]]
  (as-> db % (r reset-editor!       % editor-id)
             (r store-editor-props! % editor-id editor-props)
             (r use-initial-value!  % editor-id)
             (r use-original-value! % editor-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (if-not (r core.subs/edit-original? db editor-id)
          (let [edit-path  (r core.subs/get-editor-prop db editor-id :edit-path)
                value-path (r core.subs/get-editor-prop db editor-id :value-path)]
               (r db/copy-item! db edit-path value-path))
          (return db)))
