
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.3.6
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.events
    (:require [mid-fruits.map :as map]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (-> db ; XXX#8705
         ; Az item-lister betöltésekor felülírás nélkül aláfűzi a lister-props térképet
         ; az item-lister/meta-items térképnek, így az item-lister plugin legutóbbi
         ; beállításai elérhetők maradnak.
         (update-in [extension-id :item-lister/meta-items] map/reverse-merge lister-props)
         ; XXX#8706
         ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
         ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
         (assoc-in  [extension-id :item-lister/meta-items :item-namespace] item-namespace)))
