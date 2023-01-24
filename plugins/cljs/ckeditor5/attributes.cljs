
(ns ckeditor5.attributes
    (:require ["@ckeditor/ckeditor5-build-decoupled-document" :as ckeditor5-build-decoupled-document]
             ;["@ckeditor/ckeditor5-build-classic" :as ckeditor5-build-classic]
              [ckeditor5.helpers :as helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ckeditor-attributes
  ; @ignore
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {}
  ;
  ; @return (map)
  ; {:editor (?)
  ;  :config (map)
  ;  :data (string)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)}
  [editor-id {:keys [on-blur on-focus on-change value] :as editor-props}]
  {:editor    ckeditor5-build-decoupled-document
   :config    (helpers/ckeditor-config editor-id editor-props)
   :data      (str value)
   :on-blur   (fn [event]        (if on-blur   (on-blur   editor-id editor-props)))
   :on-focus  (fn [event]        (if on-focus  (on-focus  editor-id editor-props)))
   :on-change (fn [event editor] (if on-change (on-change editor-id editor-props (-> editor .getData))))
   :on-ready  (fn [editor]       (helpers/on-ready-f editor))})
