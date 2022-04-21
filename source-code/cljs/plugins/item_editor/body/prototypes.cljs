
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.prototypes
    (:require [mid-fruits.candy                 :refer [param]]
              [plugins.item-editor.body.config  :as body.config]
              [plugins.item-editor.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:default-view-id (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:allowed-view-ids (keywords in vector)
  ;   :default-view-id (keyword)
  ;   :item-path (vector)
  ;   :suggestions-path (vector)}
  [editor-id {:keys [default-view-id] :as body-props}]
  (merge {:allowed-view-ids [(or default-view-id body.config/DEFAULT-VIEW-ID)]
          :default-view-id  body.config/DEFAULT-VIEW-ID
          :item-path        (core.helpers/default-item-path        editor-id)
          :suggestions-path (core.helpers/default-suggestions-path editor-id)}
         (param body-props)))
