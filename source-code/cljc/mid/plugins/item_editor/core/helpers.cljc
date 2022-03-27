
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-editor.core.helpers
    (:require [mid.plugins.plugin-handler.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.plugin-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/add-item-label :my-editor :my-type)
  ;  =>
  ;  :add-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "add-" (name item-namespace))))

(defn edit-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/edit-item-label :my-editor :my-type)
  ;  =>
  ;  :edit-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "edit-" (name item-namespace))))

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/new-item-label :my-editor :my-type)
  ;  =>
  ;  :new-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "new-" (name item-namespace))))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (core.helpers/new-item-label :my-editor :my-type)
  ;  =>
  ;  :unnamed-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "unnamed-" (name item-namespace))))

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-editor)
  ;  =>
  ;  [:plugins :plugin-handler/edited-items :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :edited-items))

(defn default-suggestions-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (core.helpers/default-suggestions-path :my-editor)
  ;  =>
  ;  [:plugins :plugin-handler/suggestions :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :suggestions))
