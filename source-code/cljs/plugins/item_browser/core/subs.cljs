
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.item-lister.core.subs      :as plugins.item-lister.core.subs]
              [plugins.plugin-handler.core.subs   :as core.subs]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-router.api                   :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core.subs
(def get-description      plugins.item-lister.core.subs/get-description)
(def lister-disabled?     plugins.item-lister.core.subs/lister-disabled?)
(def get-downloaded-items plugins.item-lister.core.subs/get-downloaded-items)

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r core.subs/get-request-id db lister-id :browser))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (string)
  [db [_ _]]
  (r router/get-current-route-path-param db :item-id))

(defn get-current-item-id
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r item-browser/get-current-item-id db :my-browser)
  ;
  ; @return (string)
  [db [_ browser-id]]
  (r get-meta-item db browser-id :item-id))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#6487
  (if-let [item-path (r mount.subs/get-body-prop db browser-id :item-path)]
          (get-in db item-path)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (metamorphic-content)
  [db [_ browser-id]]
  ; XXX#6487
  ; Az [:item-browser/get-current-item-label ...] Re-Frame feliratkozás meghívható az item-browser
  ; plugin body komponensének React-fába csatolása előtt, amikor még NEM elérhetők az {:item-path ...}
  ; és {:label-key ...} paraméterek a Re-Frame adatbázisban, ezért szükséges ezen paraméterek meglétének
  ; vizsgálata!
  (let [label-key (r mount.subs/get-body-prop db browser-id :label-key)]
       (if-let [current-item (r get-current-item db browser-id)]
               (label-key current-item))))

(defn get-current-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (maps in vector)
  [db [_ browser-id]]
  ; XXX#6487
  (let [path-key (r mount.subs/get-body-prop db browser-id :path-key)]
       (if-let [current-item (r get-current-item db browser-id)]
               (-> current-item path-key vec))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r lister-disabled? db browser-id))

(defn at-home?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (let [current-item-path (r get-current-item-path db browser-id)]
       (empty? current-item-path)))

(defn get-parent-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (string)
  [db [_ browser-id]]
  (let [item-namespace    (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        current-item-path (r get-current-item-path           db browser-id)]
       (if-let [parent-link (last current-item-path)]
               (get parent-link (keyword/add-namespace item-namespace :id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (keyword) item-key
;
; @usage
;  [:item-browser/get-meta-item :my-browser :my-item]
(a/reg-sub :item-browser/get-meta-item get-meta-item)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-current-item-label :my-browser]
(a/reg-sub :item-browser/get-current-item-label get-current-item-label)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/browser-disabled? :my-browser]
(a/reg-sub :item-browser/browser-disabled? browser-disabled?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/at-home? :my-browser]
(a/reg-sub :item-browser/at-home? at-home?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-description :my-browser]
(a/reg-sub :item-browser/get-description get-description)
