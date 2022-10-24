
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.image
    (:import  [javax.imageio ImageIO]
              [javax.imageio.plugins.jpeg JPEGImageWriteParam]
              [javax.imageio.stream FileImageOutputStream]
              [javax.imageio IIOImage]
              [javax.imageio ImageWriteParam]
              [java.awt.image BufferedImage]
              [java.awt AlphaComposite]
              [java.awt Image]
              [java.io  File])
    (:require [io.api           :as io]
              [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-dimensions
  ; @param (java.awt.image.BufferedImage object) image
  ;
  ; @return (integers in vector)
  ;  [(integer) width
  ;   (integer) height]
  [image]
  [(-> image .getWidth)
   (-> image .getHeight)])

(defn scale-dimensions
  ; @param (integers in vector) size
  ; @param (integers in vector) max-size
  ;
  ; @example
  ;  (scale-dimensions [1200 600] [400 150])
  ;  =>
  ;  [300 150]
  ;
  ; @example
  ;  (scale-dimensions [400 150] [1200 600])
  ;  =>
  ;  [1200 450]
  ;
  ; @return (integers in vector)
  ;  [(integer) width
  ;   (integer) height]
  [[width height] [max-width max-height]]
  (let [ratio (max (/ width  max-width)
                   (/ height max-height))]
       [(/ width ratio) (/ height ratio)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn resize-image
  ; @param (java.awt.image.BufferedImage object) input
  ; @param (map) options
  ;  {:max-height (px)
  ;   :max-width (px)
  ;   :type-int (integer)}
  ;
  ; @return (java.awt.image.BufferedImage object)
  [input {:keys [max-height max-width type-int]}]
  (let [[input-width  input-height]  (image-dimensions input)
        [output-width output-height] (scale-dimensions [input-width input-height] [max-width max-height])
        output    (new BufferedImage output-width output-height type-int)
        graphics  (.createGraphics    output)
        temporary (.getScaledInstance input output-width output-height Image/SCALE_SMOOTH)]
       (.drawImage graphics temporary 0 0 nil)
       (.dispose   graphics)
       (return     output)))

(defn save-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (java.awt.image.BufferedImage object) input
  ; @param (string) output-path
  ; @param (map) options
  ;  {:quality (number)}
  [input output-path {:keys [quality]}]
  (let [jpeg-params   (new JPEGImageWriteParam nil)
        extension     (io/filepath->extension output-path)
        writer        (-> extension ImageIO/getImageWritersByFormatName .next)
        output-file   (clojure.java.io/file output-path)
        output-stream (new FileImageOutputStream output-file)]
       (.setCompressionMode    jpeg-params ImageWriteParam/MODE_EXPLICIT)
       (.setCompressionQuality jpeg-params quality)
       (.setOutput writer output-stream)
       (.write     writer nil (new IIOImage input nil nil) jpeg-params)))

(defn generate-thumbnail!
  ; @param (string) input-path
  ; @param (string) output-path
  ; @param (map) options
  ;  {:max-size (px)}
  ;
  ; @usage
  ;  (generate-thumbnail! "my-file.png" "my-thumbnail.png" {:max-size 512})
  [input-path output-path {:keys [max-size] :as options}]
  (let [input       (-> input-path clojure.java.io/file ImageIO/read)
        input-width (-> input .getWidth)
        mime-type   (io/filepath->mime-type input-path)
        type-int    (case mime-type "image/png" BufferedImage/TYPE_INT_ARGB BufferedImage/TYPE_INT_RGB)
        output      (resize-image input {:max-height max-size :max-width max-size :type-int type-int})
        [output-width output-height] (image-dimensions output)
        temporary (new BufferedImage output-width output-height type-int)
        graphics  (.getGraphics temporary)]
       (.drawImage graphics output 0 0 nil)
       (.dispose   graphics)
       (save-thumbnail! temporary output-path {:quality 1.0})
       (clojure.java.io/file output-path)))
