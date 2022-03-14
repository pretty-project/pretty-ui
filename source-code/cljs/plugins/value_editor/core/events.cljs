
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
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  ; A value-editor plugin minden elindulásakor kitörli a default-edit-path útvonalon található
  ; értéket, így az előző szerkesztésből esetlegesen megmaradt érték törlésre kerül.
  (let [default-edit-path (core.helpers/default-edit-path extension-id editor-id)]
       (dissoc-in db default-edit-path)))

(defn store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id editor-id editor-props]]
  (assoc-in db [extension-id :value-editor/meta-items editor-id] editor-props))

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (if-let [initial-value (r core.subs/get-meta-item db extension-id editor-id :initial-value)]
          (let [edit-path (r core.subs/get-meta-item db extension-id editor-id :edit-path)]
               (assoc-in db edit-path initial-value))
          (return db)))

(defn use-original-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  ; Ha nem az eredeti érték elérési útvonalán történik a szerkesztés, tehát az edit-path
  ; értéke nem egyenlő a value-path értékével és nincs alkalmazva initial-value érték,
  ; akkor a value-path útvonalon található érték lesz a szerkesztő mező kezdeti értéke.
  (if-not (or (r core.subs/get-meta-item  db extension-id editor-id :initial-value)
              (r core.subs/edit-original? db extension-id editor-id))
          (if-let [original-value (r core.subs/get-original-value db extension-id editor-id)]
                  (let [edit-path (r core.subs/get-meta-item      db extension-id editor-id :edit-path)]
                       (assoc-in db edit-path original-value))
                  (return db))
          (return db)))

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [event-id extension-id editor-id editor-props]]
  (as-> db % (r reset-editor!       % extension-id editor-id)
             (r store-editor-props! % extension-id editor-id editor-props)
             (r use-initial-value!  % extension-id editor-id)
             (r use-original-value! % extension-id editor-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (if-not (r core.subs/edit-original? db extension-id editor-id)
          (let [edit-path  (r core.subs/get-meta-item db extension-id editor-id :edit-path)
                value-path (r core.subs/get-meta-item db extension-id editor-id :value-path)]
               (r db/copy-item! db edit-path value-path))
          (return db)))