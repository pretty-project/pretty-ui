
(ns renderers.surface-renderer.side-effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-surface!
  [])
  ; {:autoreset-scroll-y? (boolean)(opt)
  ;   Resets the document scroll Y position when a new surface is rendered.
  ;   Default: true
  ;  :queue-behavior (keyword)
  ;   If the rendered element count exceeds the ':max-elements-rendered' property value, a new content rendering request could ...
  ;   ... be ignored.
  ;   ... push the oldest rendered content out of the queue in order to render the new content.
  ;   ... be placed at the end of the rendering queue.
  ;   :ignore, :push, :wait
  ;   Default: :push
  ;  :rerender-same? (boolean)
  ;   If a content rendering request has the same ID as an already rendered content
  ;   this property determines whether the new content replaces the old one.
  ;   Default: false
