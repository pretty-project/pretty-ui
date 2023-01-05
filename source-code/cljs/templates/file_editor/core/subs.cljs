
(ns templates.file-editor.core.subs
    (:require [engines.file-editor.core.subs     :as core.subs]
              [engines.file-editor.download.subs :as download.subs]
              [re-frame.api                      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.file-editor.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def editor-synchronizing? core.subs/editor-synchronizing?)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (boolean)
  [db [_ editor-id]]
  ; XXX#3219 (source-code/cljs/templates/item_handler/core/subs.cljs)
  (let [data-received?        (r download.subs/data-received? db editor-id)
        editor-synchronizing? (r editor-synchronizing?        db editor-id)]
       (or editor-synchronizing? (not data-received?))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
(r/reg-sub :file-editor/editor-disabled? editor-disabled?)
