
(ns templates.item-preview.body.views
    (:require [components.api                         :as components]
              [elements.api                           :as elements]
              [engines.item-preview.api               :as item-preview]
              [random.api                             :as random]
              [reagent.api                            :refer [component?]]
              [templates.item-preview.body.prototypes :as body.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- downloading-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [_ _]
  [elements/label {:color     :muted
                   :content   :downloading...
                   :font-size :xs}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; {:import-id-f (function)
  ;  :item-link (namespaced map)
  ;  :item-path (vector)
  ;  :preview-element (component or symbol)
  ;  :transfer-id (keyword)}
  [preview-id {:keys [import-id-f item-link item-path preview-element transfer-id] :as preview-props}]
  ; BUG#9980
  ; Az item-preview/body komponenseket különböző azonosítókkal szükséges meghívni,
  ; hogy külön tudják kezelni az egyes elemekhez letöltött adatokat!
  ;
  ; A kiválasztott elem azonosítóját nem lehet használni az item-preview/body komponens
  ; azonosítójaként!
  ; Pl.: Ha van két item-preview/body komponens és mindkettőben ugyanaz az elem van
  ;      kiválasztva, a React-fába csatolódásukkor, akkor ugyanazt az azonosítót használnák!
  ;
  ; Generált azonosítót sem lehetséges használni, mert a komponens újrarenderelődésekor
  ; (pl. a preview-props térkép megváltozásakor), az item-preview engine példánya
  ; elveszítené a kapcsolatot a letöltött adatokkal!
  (let [id (import-id-f item-link)]
       [item-preview/body preview-id
                          {:error-element   [components/error-label {:content :the-content-has-been-broken}]
                           :ghost-element   [downloading-label preview-id preview-props]
                           :preview-element [preview-element   preview-id preview-props]
                           :item-id         id
                           :item-path       item-path
                           :transfer-id     transfer-id}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-preview-placeholder
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; {:disabled? (boolean)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [preview-id {:keys [disabled? placeholder] :as preview-props}]
  ; XXX#9010
  ; Az item-preview komponensnek komponensként vagy szimbólumnként is át lehet
  ; adni a placeholder tulajdonságot.
  (cond (component? placeholder) (conj placeholder preview-id preview-props)
        (fn?        placeholder) [placeholder      preview-id preview-props]
        (some?      placeholder) [elements/label {:color       :muted
                                                  :content     placeholder
                                                  :disabled?   disabled?
                                                  :font-size   :xs
                                                  :selectable? true}]))

(defn- item-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; {:indent (map)(opt)
  ;  :import-id-f (function)
  ;  :item-link (namespaced map)(opt)
  ;  :outdent (map)(opt)}
  [preview-id {:keys [import-id-f indent item-link outdent] :as preview-props}]
  ; BUG#8860
  ; Akkor jeleníti meg az item-preview-body komponenst ha az item-link értéke tartalmazza
  ; az id kulcsot, így ha az item-link értéke nil, vagy hibás/hiányos, akkor a placeholder
  ; felirat jelenik meg.
  [elements/blank preview-id
                  {:content [:<> [elements/element-label preview-id preview-props]
                                 (if-let [id (import-id-f item-link)]
                                         [item-preview-body        preview-id preview-props]
                                         [item-preview-placeholder preview-id preview-props])]
                   :indent  indent
                   :outdent outdent}])

(defn body
  ; @warning
  ; Pass the preview-id!
  ; BUG#9980
  ;
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :import-id-f (function)(opt)
  ;   Default: (fn [{:keys [id] :as item-link}] id)
  ;  :indent (map)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :item-link (namespaced map)(opt)
  ;  :item-path (vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preview-element (component or symbol)
  ;  :transfer-id (keyword)(opt)
  ;   Az elemet megjelenítő engines.item-preview.api/body komponens paramétere.
  ;
  ; @usage
  ; [body {...}]
  ;
  ; @usage
  ; [body :my-item-preview {...}]
  ;
  ; @usage
  ; (defn my-preview-element [preview-id preview-props] ...)
  ; [body :my-item-preview {:preview-element #'my-preview-element}]
  ;
  ; @usage
  ; [body :my-item-preview {:placeholder "My placeholder"}]
  ;
  ; @usage
  ; (defn my-placeholder [preview-id preview-props] ...)
  ; [body :my-item-preview {:placeholder #'my-placeholder}]
  ([preview-props]
   [body (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (body.prototypes/preview-props-prototype preview-id preview-props)]
        [item-preview preview-id preview-props])))
