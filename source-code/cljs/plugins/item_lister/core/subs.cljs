
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.subs
    (:require [mid-fruits.logical   :refer [nor]]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lister-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-lister/meta-items]))

(defn get-inherited-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin ...
  ; ... az első betöltődésekor letölti az elemeket az alapbeállításokkal.
  ; ... a további betöltődésekkor ...
  ;     ... letölti az elemeket a legutóbb használt keresési és rendezési beállításokkal, így a felhasználó
  ;         az egyes elemek megtekintése/szerkesztése/... után visszatérhet a legutóbbi kereséséhez!
  ;     ... megőrzi a plugin szerver-oldalról érkezett beállításait.
  (let [lister-props (r get-lister-props db extension-id item-namespace)]
       (select-keys lister-props [:on-load :route-template :route-title ; <- szerver-oldalról érkezett beállítások
                                  :order-by :search-term])))            ; <- keresési és rendezési beállítások

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (get-in db [extension-id :item-lister/meta-items item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (r get-request-id db :my-extension :my-type)
  ;  =>
  ;  :my-handler/synchronize-lister!
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace]]
  ; XXX#3055
  ; A komponensek [:item-lister/lister-synchronizing? ...] feliratkozása már azelőtt megpróbálja
  ; kiolvasni a Re-Frame adatbázisból a handler-key értékét, mielőtt az eltárolásra kerülne ...
  (if-let [handler-key (r get-meta-item db extension-id item-namespace :handler-key)]
          (keyword (name handler-key) "synchronize-lister!")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; XXX#0499
  ; A szerverrel való első kommunkáció megtörténtét, nem lehetséges az (r sync/request-sent? db ...)
  ; függvénnyel vizsgálni, mert ha az item-lister már meg volt jelenítve, akkor az újbóli
  ; megjelenítéskor (r sync/request-sent? db ...) függvény visszatérési értéke true lenne!
  (let [items-received? (r core.subs/get-meta-item db extension-id item-namespace :items-received?)]
       (boolean items-received?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (r get-request-id db extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn lister-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [items-received?       (r items-received?       db extension-id item-namespace)
        lister-synchronizing? (r lister-synchronizing? db extension-id item-namespace)]
       ; XXX#3219
       (or lister-synchronizing? (not items-received?))))

(defn items-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-actions (r get-meta-item db extension-id item-namespace :item-actions)]
       (vector/nonempty? item-actions)))

(defn items-sortable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :sortable?))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id item-namespace)
        all-item-count        (r get-all-item-count        db extension-id item-namespace)
        items-received?       (r items-received?           db extension-id item-namespace)]
       (if items-received? (components/content {:content      :npn-items-downloaded
                                                :replacements [downloaded-item-count all-item-count]}))))

(defn get-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (keywords in vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-actions))

(defn get-new-item-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :new-item-options))

(defn get-new-item-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-event)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :new-item-event))

(defn get-list-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :list-element))

(defn get-menu-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :menu-element))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :error-mode?))

(defn menu-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [reorder-mode? (r get-meta-item db extension-id item-namespace :reorder-mode?)
        search-mode?  (r get-meta-item db extension-id item-namespace :search-mode?)]
       (nor reorder-mode? search-mode?)))

(defn reorder-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :reorder-mode?))

(defn search-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :search-mode?))

(defn select-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :select-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-filter-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:$and (maps in vector)}
  [db [_ extension-id item-namespace]]
  (let [active-filter (r get-meta-item db extension-id item-namespace :active-filter)
            prefilter (r get-meta-item db extension-id item-namespace     :prefilter)]
       (cond-> {} active-filter (update :$and vector/conj-item active-filter)
                      prefilter (update :$and vector/conj-item     prefilter))))

(defn get-search-term
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [search-term (r get-meta-item db extension-id item-namespace :search-term)]
       (str search-term)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (keyword) item-key
;
; @usage
;  [:item-lister/get-meta-item :my-extension :my-type :my-item]
(a/reg-sub :item-lister/get-meta-item get-meta-item)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-description :my-extension :my-type]
(a/reg-sub :item-lister/get-description get-description)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-item-actions :my-extension :my-type]
(a/reg-sub :item-lister/get-item-actions get-item-actions)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-new-item-options :my-extension :my-type]
(a/reg-sub :item-lister/get-new-item-options get-new-item-options)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-new-item-event :my-extension :my-type]
(a/reg-sub :item-lister/get-new-item-event get-new-item-event)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-list-element :my-extension :my-type]
(a/reg-sub :item-lister/get-list-element get-list-element)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-menu-element :my-extension :my-type]
(a/reg-sub :item-lister/get-menu-element get-menu-element)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/lister-disabled? :my-extension :my-type]
(a/reg-sub :item-lister/lister-disabled? lister-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/items-selectable? :my-extension :my-type]
(a/reg-sub :item-lister/items-selectable? items-selectable?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/items-sortable? :my-extension :my-type]
(a/reg-sub :item-lister/items-sortable? items-sortable?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/error-mode? :my-extension :my-type]
(a/reg-sub :item-lister/error-mode? error-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/menu-mode? :my-extension :my-type]
(a/reg-sub :item-lister/menu-mode? menu-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/reorder-mode? :my-extension :my-type]
(a/reg-sub :item-lister/reorder-mode? reorder-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/search-mode? :my-extension :my-type]
(a/reg-sub :item-lister/search-mode? search-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/select-mode? :my-extension :my-type]
(a/reg-sub :item-lister/select-mode? select-mode?)
