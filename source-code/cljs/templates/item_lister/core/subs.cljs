
(ns templates.item-lister.core.subs
    (:require [engines.item-lister.core.subs     :as core.subs]
              [engines.item-lister.download.subs :as download.subs]
              [re-frame.api                      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def lister-synchronizing? core.subs/lister-synchronizing?)
(def no-items-to-show?     core.subs/no-items-to-show?)
(def downloading-items?    core.subs/downloading-items?)
(def all-items-downloaded? core.subs/all-items-downloaded?)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-disabled?
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [data-received?        (r download.subs/data-received? db lister-id)
        lister-synchronizing? (r lister-synchronizing?        db lister-id)]
       ; XXX#3219 (source-code/cljs/templates/item_handler/core/subs.cljs)
       (or lister-synchronizing? (not data-received?))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn display-error?
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (and (r get-meta-item     db lister-id :engine-error)
       (r no-items-to-show? db lister-id)))

(defn display-ghost?
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; Why {:data-received? false} state causes the rendering of the ghost component?
  ; - Before start downloading the data, the ghost component has to be shown,
  ;   otherwise it will only shown up after the body component get rendered and it
  ;   would be shown up after a short flicker.
  (let [display-error?        (r display-error?               db lister-id)
        all-items-downloaded? (r all-items-downloaded?        db lister-id)
        data-received?        (r download.subs/data-received? db lister-id)]
       ; (and (not display-error?)
       ;      (not (and all-items-downloaded? data-received?)))
       (not (or display-error? (and all-items-downloaded? data-received?)))))

(defn display-placeholder?
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; Szükséges a data-received? értékét is vizsgálni, hogy az adatok letöltésének
  ; elkezdése előtti pillanatban ne villanjon fel a placeholder komponens!
  ;
  ; Szükséges a 'downloading-items?' értékét is vizsgálni, hogy az adatok
  ; letöltése közben ne jelenjen meg a placeholder komponens!
  (let [display-error?     (r display-error?               db lister-id)
        downloading-items? (r downloading-items?           db lister-id)
        no-items-to-show?  (r no-items-to-show?            db lister-id)
        data-received?     (r download.subs/data-received? db lister-id)]
       (and no-items-to-show? data-received? (not downloading-items?)
                                             (not display-error?))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
(r/reg-sub :item-lister/lister-disabled? lister-disabled?)

; @param (keyword) lister-id
(r/reg-sub :item-lister/display-error? display-error?)

; @param (keyword) lister-id
(r/reg-sub :item-lister/display-ghost? display-ghost?)

; @param (keyword) lister-id
(r/reg-sub :item-lister/display-placeholder? display-placeholder?)
