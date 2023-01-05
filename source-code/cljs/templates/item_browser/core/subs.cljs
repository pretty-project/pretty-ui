
(ns templates.item-browser.core.subs
    (:require [templates.item-lister.core.subs :as core.subs]
              [re-frame.api                    :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.core.subs
(def lister-disabled? core.subs/lister-disabled?)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-disabled?
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  ; XXX#3219 (source-code/cljs/templates/item_handler/core/subs.cljs)
  (r lister-disabled? db browser-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
(r/reg-sub :item-browser/browser-disabled? browser-disabled?)
